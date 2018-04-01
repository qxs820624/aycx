package com.liyun.blelibrary;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Rob D., Qinlei C.
 * @create 2015/8/13.
 */
@SuppressLint("NewApi")
public abstract class BluetoothLeDevice {

    private static final String TAG = "c.s.i.b.BLeDevice";

    private final ScheduledExecutorService mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * Enumeration of connection status
     */
    public enum State {
        CONNECTING, CONNECTED, SERVICE_DISCOVERED, DISCONNECTED
    }

    // region broadcast
    public static final String ACTION_CONNECTING          = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_CONNECTING";
    public static final String ACTION_CONNECTED           = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_CONNECTED";
    public static final String ACTION_SERVICES_DISCOVERED = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_SERVICES_DISCOVERED";
    public static final String ACTION_DISCONNECTED        = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_DISCONNECTED";
    public static final String ACTION_CONNECT_TIMEOUT     = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_CONNECT_TIMEOUT";
    public static final String ACTION_RSSI                = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_RSSI";
    public static final String ACTION_DEVICE_VERSION      = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_ACTION_DEVICE_VERSION";
    public static final String ACTION_DEVICE_MODEL_NAME      = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_ACTION_DEVICE_MODEL_NAME";
    public static final String ACTION_DEVICE_SOFT_VERSION      = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_ACTION_DEVICE_SOFT_VERSION";
    public static final String ACTION_DEVICE_HARD_VERSION      = "com.sennotech.kisscat.ble.BluetoothLeDevice.ACTION_ACTION_DEVICE_HARD_VERSION";
    public static final String EXTRA_NAME                 = "com.sennotech.kisscat.ble.BluetoothLeDevice.EXTRA_NAME";
    public static final String EXTRA_DATA                 = "com.sennotech.kisscat.ble.BluetoothLeDevice.EXTRA_DATA";
    // endregion
    public static final String ACTION_AUDIO_SPEED        = "BluetoothLeDevice.AUDIO_SPEED";
    protected static final UUID UUID_CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private static final long DELAY_OF_CONNECT_TIMEOUT = 15_000;

    private static final int WHAT_CONNECT_DEVICE    = 1000;
    private static final int WHAT_DISCOVER_SERVICE  = 1002;
    private static final int WHAT_DISCONNECT_DEVICE = 1003;
    private static final int WHAT_CLOSE_DEVICE      = 1004;

    private Handler mHandler;
    protected final Context mContext;
    protected final BluetoothDevice mBluetoothDevice;
    public final String name;
    public final String address;

    private final Queue<BluetoothGattElement> mSendQueue = new ConcurrentLinkedQueue<>();

    private ScheduledFuture<?> mScheduledFuture;

    protected BluetoothGatt mBluetoothGatt;

    private State   mState     = State.DISCONNECTED;
    private boolean mIsSending = false;

    private BluetoothLeDevice() {
        mContext = null;
        mBluetoothDevice = null;
        name = "N/A";
        address = "N/A";
    }

    protected BluetoothLeDevice(Context context, BluetoothDevice device) {
        this(context, device, device.getName());
    }

    protected BluetoothLeDevice(Context context, BluetoothDevice device, String name) {
        if (context == null || device == null) {
            throw new IllegalArgumentException();
        }

        this.mContext = context;
        this.mBluetoothDevice = device;
        this.name = name;
        this.address = device.getAddress();

        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(mContext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_CONNECT_DEVICE:
                        //连接设备
                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.close();
                            mBluetoothGatt = null;
                        }
                        mBluetoothGatt = mBluetoothDevice.connectGatt(mContext, false, new CommonBluetoothGattCallback(createBluetoothDataHandler()));
                        Log.d(TAG, "handleMessage() called with: " + "mBluetoothGatt = [" + mBluetoothGatt + "]");

                        startConnectTimeOut();  //蓝牙连接超时等待
                        changeState(State.CONNECTING);
                        break;
                    case WHAT_DISCOVER_SERVICE:
                        if (mState == State.CONNECTED && mBluetoothGatt != null) {
                            mBluetoothGatt.discoverServices();
                        } else {
                            Log.w(TAG, "failed to discoverService, illegal state [" + mState.name() + "]");
                        }
                        break;
                    case WHAT_DISCONNECT_DEVICE:
                        //断开连接,这里不改变状态,会默认触发连接断开的回调
                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.disconnect();
                            Log.d(TAG, "handleMessage() called with: mBluetoothGatt had disconnect !");
                        } else {
                            Log.e(TAG, "handleMessage: WHAT_DISCONNECT_DEVICE fail to disconnect!");
                        }
                        break;
                    case WHAT_CLOSE_DEVICE:
                        //关闭蓝牙
                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.close();
                            mBluetoothGatt = null;
                            changeState(State.DISCONNECTED);
                        } else {
                            Log.e(TAG, "handleMessage: WHAT_CLOSE_DEVICE fail to close!");
                        }
                        break;
                    default:
                        break;
                }

                super.handleMessage(msg);
            }
        };
    }

    /**
     * 蓝牙连接超时
     */
    private void startConnectTimeOut() {
        if (mScheduledFuture != null) {
            mScheduledFuture.cancel(true);
            mScheduledFuture = null;
        }

        mScheduledFuture = mScheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                if (mState != State.SERVICE_DISCOVERED && mState != State.DISCONNECTED) {
                    Log.d(TAG, "连接超时  " + mState);
                    mContext.sendBroadcast(new Intent(ACTION_CONNECT_TIMEOUT).putExtra(EXTRA_NAME, name));
                    mHandler.sendEmptyMessage(WHAT_DISCONNECT_DEVICE);
                }
            }
        }, DELAY_OF_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 连接蓝牙设备（当前状态必须是未连接）
     */
    public synchronized void connect() {
        if (mState == State.DISCONNECTED) {
            mHandler.sendEmptyMessage(WHAT_CONNECT_DEVICE);
        } else {
            Log.e(TAG, "failed to connect, illegal state [" + mState.name() + "]");
        }
    }

    public synchronized void disconnect() {
        mHandler.sendEmptyMessage(WHAT_DISCONNECT_DEVICE);
    }

    /**
     * Disconnect , Close and release the bluetooth
     */
    public synchronized void close() {
        mHandler.sendEmptyMessage(WHAT_CLOSE_DEVICE);
    }

    /**
     * Update the connection status
     */
    private synchronized void changeState(State newState) {
        Log.d(TAG, "changeState() called with: " + "newState = [" + newState + "]");
        if (mState == newState) {
            return;
        }

        mState = newState;  //更新蓝牙当前状态

        if (newState == State.CONNECTING) {
            mContext.sendBroadcast(new Intent().setAction(ACTION_CONNECTING).putExtra(EXTRA_NAME, name));
        } else if (newState == State.CONNECTED) {
            mContext.sendBroadcast(new Intent(ACTION_CONNECTED).putExtra(EXTRA_NAME, name));
        } else if (newState == State.SERVICE_DISCOVERED) {
            mContext.sendBroadcast(new Intent(ACTION_SERVICES_DISCOVERED).putExtra(EXTRA_NAME, name));
        } else if (newState == State.DISCONNECTED) {
            mContext.sendBroadcast(new Intent(ACTION_DISCONNECTED).putExtra(EXTRA_NAME, name));
        }
    }

    /**
     * Get the connection status
     */
    public synchronized State getState() {
        return mState;
    }

    /**
     * Write commands to device
     *
     * @param serviceUuid        UUID of service
     * @param characteristicUuid UUID of characteristic
     * @param value              Command to write
     */
    protected void writeCharacteristic(UUID serviceUuid, UUID characteristicUuid, byte[] value) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(serviceUuid);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                if (characteristic != null) {
                    characteristic.setValue(value);
                    send(new BluetoothGattElement(characteristic, BluetoothGattElement.WRITE));
                } else {
                    Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but doesn't exist");
                }
            } else {
                Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but service [" + serviceUuid + "] doesn't exist");
            }
        } else {
            Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but gatt is null");
        }
    }

    /**
     * Get Characteristic data from local data
     *
     * @param serviceUuid        UUID of service
     * @param characteristicUuid UUID of characteristic
     */
    protected byte[] readCharacteristicLocally(UUID serviceUuid, UUID characteristicUuid) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(serviceUuid);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                if (characteristic != null) {
                    return characteristic.getValue();
                } else {
                    Log.w(TAG, name + " read characteristic [" + characteristicUuid + "], but doesn't exist");
                    return new byte[]{};
                }
            } else {
                Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but service [" + serviceUuid + "] doesn't exist");
                return new byte[]{};
            }
        } else {
            Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but gatt is null");
            return new byte[]{};
        }
    }

    /**
     * Get Characteristic data from remote data
     *
     * @param serviceUuid        UUID of service
     * @param characteristicUuid UUID of characteristic
     */
    protected void readCharacteristicRemotely(UUID serviceUuid, UUID characteristicUuid) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(serviceUuid);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                if (characteristic != null) {
                    send(new BluetoothGattElement(characteristic, BluetoothGattElement.READ));
                    Log.d(TAG, "send read remote characteristic [" + characteristicUuid + "] command, thread -> " + Thread.currentThread().getName());
                } else {
                    Log.w(TAG, name + " read characteristic [" + characteristicUuid + "], but doesn't exist");
                }
            } else {
                Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but service [" + serviceUuid + "] doesn't exist");
            }
        } else {
            Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but gatt is null");
        }
    }

    /**
     * Notify Characteristic data to device
     *
     * @param serviceUuid        UUID of service
     * @param characteristicUuid UUID of characteristic
     * @param enable             true=enable, false=disable
     */
    protected void notifyCharacteristic(UUID serviceUuid, UUID characteristicUuid, boolean enable) {
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(serviceUuid);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                if (characteristic != null) {
                    BluetoothGattDescriptor ccc = characteristic.getDescriptor(UUID_CCC);
                    if (ccc != null) {
                        boolean success = mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
                        if (success) {
                            Log.d(TAG, name + " succeeded to set characteristic [" + characteristic + "] notification");
                            ccc.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE :
                                    BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                            send(new BluetoothGattElement(ccc, BluetoothGattElement.WRITE));
                        } else {
                            Log.w(TAG, name + " write characteristic [" + characteristic + "], but failed");
                        }
                    } else {
                        Log.w(TAG, name + " write characteristic [" + characteristic + "], but descriptor [" + UUID_CCC + "] doesn't exist");
                    }
                } else {
                    Log.w(TAG, name + " write characteristic [" + characteristic + "], but doesn't exist");
                }
            } else {
                Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but service [" + serviceUuid + "] doesn't exist");
            }
        } else {
            Log.w(TAG, name + " write characteristic [" + characteristicUuid + "], but gatt is null");
        }
    }

    /**
     * Send BluetoothGattElement (add into Blocking Queue when busy)
     *
     * @param element BluetoothGattElement to be sent
     */
    private synchronized void send(BluetoothGattElement element) {
        if (mSendQueue.isEmpty() && !mIsSending) {
            doSend(element);
        } else {
            if (element.element instanceof BluetoothGattCharacteristic) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) element.element;
            } else if (element.element instanceof BluetoothGattDescriptor) {
                BluetoothGattDescriptor descriptor = (BluetoothGattDescriptor) element.element;
            } else {
                Log.w(TAG, "you send unrecognized type [" + element.element.getClass().getName() + "]");
                return;
            }
            mSendQueue.offer(element);
        }
    }

    /**
     * Send BluetoothGattElement
     *
     * @param element BluetoothGattElement to be sent
     */
    private synchronized void doSend(BluetoothGattElement element) {
        if (element.element instanceof BluetoothGattCharacteristic) {
            mIsSending = true;

            BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) element.element;
            if (element.type == BluetoothGattElement.WRITE) {
                boolean success = mBluetoothGatt.writeCharacteristic(characteristic);
            } else if (element.type == BluetoothGattElement.READ) {
                boolean success = mBluetoothGatt.readCharacteristic(characteristic);
            }
        } else if (element.element instanceof BluetoothGattDescriptor) {
            mIsSending = true;

            BluetoothGattDescriptor descriptor = (BluetoothGattDescriptor) element.element;
            if (element.type == BluetoothGattElement.WRITE) {
                boolean success = mBluetoothGatt.writeDescriptor(descriptor);
            } else if (element.type == BluetoothGattElement.READ) {
                boolean success = mBluetoothGatt.readDescriptor(descriptor);
            }
        } else {
            Log.w(TAG, "you send unrecognized type [" + element.element.getClass().getName() + "]");
        }
    }

    /**
     * send next BluetoothGattElement
     */
    private synchronized void nextSend() {
        if (!mSendQueue.isEmpty() && !mIsSending) {
            doSend(mSendQueue.poll());
        }
    }

    protected abstract BluetoothDataHandler createBluetoothDataHandler();

    /**
     * BluetoothGattElement class
     */
    private class BluetoothGattElement {

        private static final int WRITE = 0;
        private static final int READ  = 1;

        public final Object element;
        public final int    type;

        public BluetoothGattElement(Object element, int type) {
            this.element = element;
            this.type = type;
        }
    }

    public static abstract class BluetoothDataHandler extends BluetoothGattCallback {

        @Override
        public final void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        }

        @Override
        public final void onServicesDiscovered(BluetoothGatt gatt, int status) {
        }

        @Override
        public final void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public final void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        }

        @Override
        public final void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        }
    }

    private class CommonBluetoothGattCallback extends BluetoothGattCallback {

        private final BluetoothDataHandler mBluetoothDataHandler;

        private CommonBluetoothGattCallback(BluetoothDataHandler handler) {
            this.mBluetoothDataHandler = handler;
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "onConnectionStateChange() called with: " + "gatt = [" + gatt + "], status = [" + status + "], newState = [" + newState + "]");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "bluetooth device [" + name + "] connection state changed, reported by " + this);
                switch (newState) {
                    case BluetoothProfile.STATE_CONNECTED:
                        if (State.CONNECTING == mState) {
                            changeState(State.CONNECTED);   //改变当前状态为已连接
                            mHandler.sendEmptyMessage(WHAT_DISCOVER_SERVICE);   //去发现服务
                        }
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        changeState(State.DISCONNECTED);    //改变当前状态为已断开
                        break;
                    default:
                        Log.w(TAG, name + "'s connection state has changed, previous state [" + status + "], current state [" + newState + "]");
                        break;
                }
            } else if (status == 62) {
                // TODO: 2016/8/13 曾经收到一个62的状态值，有待处理
            } else if (status == 8) {
                // TODO: 2016/8/13 曾经收到一个8的状态值，有待处理
                changeState(State.DISCONNECTED);
            } else if (status == 19) {
                // TODO: 2016/8/20 固件更新完毕会发19的状态值
                changeState(State.DISCONNECTED);
            } else if (status == 133) {
                // TODO: 2016/8/16 曾经收到一个133的状态值，有待处理
            } else {
                //Todo 默认收到其他状态后的处理
                Log.d(TAG, "reconnect start and " + " status = [" + status + "], newState = [" + newState + "]");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, name + " had discovered services");
                if (mScheduledFuture != null) {
                    mScheduledFuture.cancel(true);
                    mScheduledFuture = null;
                }

                changeState(State.SERVICE_DISCOVERED);
            } else {
                Log.w(TAG, name + " did not discover services, status [" + status + "]");
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mContext.sendBroadcast(new Intent(ACTION_RSSI)
                        .putExtra(EXTRA_NAME, name)
                        .putExtra(EXTRA_DATA, rssi));
            } else {
                Log.w(TAG, name + "{} failed to read rssi feedback");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            mBluetoothDataHandler.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            mBluetoothDataHandler.onCharacteristicRead(gatt, characteristic, status);

            mIsSending = false;
            nextSend();
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            mBluetoothDataHandler.onCharacteristicWrite(gatt, characteristic, status);

            mIsSending = false;
            nextSend();
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            mBluetoothDataHandler.onDescriptorWrite(gatt, descriptor, status);

            mIsSending = false;
            nextSend();
        }
    }
}