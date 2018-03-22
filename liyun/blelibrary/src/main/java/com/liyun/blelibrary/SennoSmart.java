package com.liyun.blelibrary;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.liyun.blelibrary.listener.SmartMotionDataCallback;
import com.liyun.blelibrary.listener.SmartPedometerDataCallback;
import com.liyun.blelibrary.listener.SmartSystemDataCallback;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author chen.qinlei
 * @create 2016-05-20 17:19.
 */
public class SennoSmart extends BluetoothLeDevice {

    private static final String TAG = "SennoSmart";

    // region uuid
    public static final UUID UUID_SERVICE_DEVICE_INFO             = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_NUMBER_NAME = UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_VERSION = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_HARDWARE_VERSION = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_SOFTWARE_VERSION = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");


    public static final UUID UUID_SERVICE_DATA = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_FFF5 = UUID.fromString("0000fff5-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_FFF6 = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_CHARACTERISTIC_FFF7 = UUID.fromString("0000fff7-0000-1000-8000-00805f9b34fb");
    //////////////////////////////////////////////////////////////////////////////////////////

    public static final UUID UUID_SERVICE_SETTING                         = UUID.fromString("33000000-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_SETTING_TIME             = UUID.fromString("33000001-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_GETTING_BATTERY          = UUID.fromString("33000002-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_CONNECTION_PARAM_SETTING = UUID.fromString("33000003-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_CONNECTION_PARAM         = UUID.fromString("33000004-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_CONTROL = UUID.fromString("33000005-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_DATA    = UUID.fromString("33000006-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_STATUS  = UUID.fromString("33000007-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_TIME    = UUID.fromString("33000008-121F-3E53-656E-6E6F54656368");

    public static final UUID UUID_SERVICE_PEDOMETER                       = UUID.fromString("33002100-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_PEDOMETER_STATUS         = UUID.fromString("33002101-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_PEDOMETER_RECORD_STATUS  = UUID.fromString("33002102-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_PEDOMETER_RECORD_CONTROL = UUID.fromString("33002103-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_PEDOMETER_RECORD_DATA    = UUID.fromString("33002104-121F-3E53-656E-6E6F54656368");

    public static final UUID UUID_SERVICE_GAME                     = UUID.fromString("33002000-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_MOTION_RECOGNIZER = UUID.fromString("33002001-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_MOTION_CONTINUITY = UUID.fromString("33002002-121F-3E53-656E-6E6F54656368");
    public static final UUID UUID_CHARACTERISTIC_MOTION_STOP       = UUID.fromString("33002003-121F-3E53-656E-6E6F54656368");
    // endregion

    private static final byte VALUE_UPGRADE_ACTIVE = 0x00;
    private static final byte VALUE_UPGRADE_ENABLE = 0x01;

    public SennoSmart(Context context, BluetoothDevice device) {
        super(context, device);
    }

    public SennoSmart(Context context, BluetoothDevice device, String name) {
        super(context, device, name);
    }

    private SmartMotionDataCallback mSmartMotionDataCallback;
    private SmartSystemDataCallback mSmartSystemDataCallback;
    private SmartPedometerDataCallback mSmartPedometerDataCallback;

    public void controlNotifyMotionData(SmartMotionDataCallback smartMotionDataCallback, boolean control) {
        this.mSmartMotionDataCallback = smartMotionDataCallback;

        notifyCharacteristic(UUID_SERVICE_GAME, UUID_CHARACTERISTIC_MOTION_RECOGNIZER, control);
        notifyCharacteristic(UUID_SERVICE_GAME, UUID_CHARACTERISTIC_MOTION_CONTINUITY, control);
        notifyCharacteristic(UUID_SERVICE_GAME, UUID_CHARACTERISTIC_MOTION_STOP, control);
    }

    /**
     * Write the current timestamp to InsoleX (in write mode)
     */
    public void synchronizeTimeStamp() {
        byte[] timeStamp = convertTimestamp(new Date());
        writeCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_SETTING_TIME, timeStamp);
    }

    /**
     * Convert timestamp into a byte array
     */
    private byte[] convertTimestamp(Date datetime) {
        int timestamp = (int) (datetime.getTime() / 1000);
        return new byte[]{(byte) timestamp, (byte) (timestamp >> 8), (byte) (timestamp >> 16), (byte) (timestamp >> 24)};
    }

    /**
     * Get Battery Voltage (in read mode)
     */
    public void readBattery() {
        readCharacteristicRemotely(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_GETTING_BATTERY);
    }

    /**
     * Get Connection Param (in read mode)
     */
    public void readConnectionParam() {
        readCharacteristicRemotely(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_CONNECTION_PARAM);
    }

    /**
     * Get Connection Param (in notify mode)
     *
     * @param control
     */
    public void notifyConnectionParam(boolean control) {
        notifyCharacteristic(UUID_SERVICE_DATA, UUID_CHARACTERISTIC_FFF6, control);
    }

    /**
     * write firmware upgrade control order
     *
     * @param length  firmware length
     */
    public void activeUpgrade(int length) {
        byte[] data = encodeUpgradeCommand(VALUE_UPGRADE_ACTIVE, 0xc000 - 16, length);
        writeCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_CONTROL,
                data);
    }

    private byte[] encodeUpgradeCommand(byte command, int address, int length) {
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.order(ByteOrder.LITTLE_ENDIAN);

        bb.put(command);
        bb.putInt(address);
        bb.putInt(length);

        return bb.array();
    }

    public void enableUpgrade() {
        byte[] data = encodeUpgradeCommand(VALUE_UPGRADE_ENABLE, 0, 0);
        writeCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_CONTROL,
                data);
    }

    public void writeFirmwareUpgradeData(int address, byte[] data) {
        byte[] order = convertFirmwareUpgradeData(address, data);
        writeCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_DATA, order);
    }

    private byte[] convertFirmwareUpgradeData(int address, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(address);
        buffer.put(data);

        return buffer.array();
    }

    /**
     * 获取固件版本号
     */
    public void readFirmwareVersion() {
        readCharacteristicRemotely(UUID_SERVICE_DEVICE_INFO, UUID_CHARACTERISTIC_FIRMWARE_VERSION);
    }
    /**
     * 获取设备名称
     */
    public void readModelNumber() {
        readCharacteristicRemotely(UUID_SERVICE_DEVICE_INFO, UUID_CHARACTERISTIC_NUMBER_NAME);
    }
    /**
     * 获取硬件版本
     */
    public void readHardwareVersion() {
        readCharacteristicRemotely(UUID_SERVICE_DEVICE_INFO, UUID_CHARACTERISTIC_HARDWARE_VERSION);
    }
    /**
     * 获取软件版本
     */
    public void readSoftVersion() {
        readCharacteristicRemotely(UUID_SERVICE_DEVICE_INFO, UUID_CHARACTERISTIC_SOFTWARE_VERSION);
    }
    /**
     * 获取固件更新的时间
     */
    public void readFirmwareUpdateTime() {
        readCharacteristicRemotely(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_TIME);
    }

    public void notifyFirmwareUpgradeStatus(boolean control) {
        notifyCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_STATUS, control);
    }

    /**
     * Get Pedometer Status (in notify mode)
     *
     * @param control
     */
    public void notifyPedometerStatus(boolean control) {
        notifyCharacteristic(UUID_SERVICE_PEDOMETER, UUID_CHARACTERISTIC_PEDOMETER_STATUS, control);
    }

    /**
     * 获取计步数据的状态值（开始ID和结束ID）
     */
    public void readPedometerRecordStatus() {
        readCharacteristicRemotely(UUID_SERVICE_PEDOMETER, UUID_CHARACTERISTIC_PEDOMETER_RECORD_STATUS);
    }

    /**
     * 通过传输一个开始ID去同步数据
     *
     * @param startId
     */
    public void startSynchronizePedometerRecordData(int startId) {
        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) 0x00);
        buffer.putInt(startId);

        writeCharacteristic(UUID_SERVICE_PEDOMETER, UUID_CHARACTERISTIC_PEDOMETER_RECORD_CONTROL, buffer.array());
        Log.d(TAG, "startSynchronizePedometerRecordData() called with: " + "startId = [" + startId + "]");
    }

    /**
     * 打开同步计步记录数据的notify通道
     *
     * @param control
     */
    public void notifyPedometerRecordData(boolean control) {
        notifyCharacteristic(UUID_SERVICE_PEDOMETER, UUID_CHARACTERISTIC_PEDOMETER_RECORD_DATA, control);
    }

    /**
     * 更新链接参数
     *
     * @param minInterval
     * @param maxInterval
     * @param latency
     * @param timeout
     */
    public void updateConnectionSetting(double minInterval, double maxInterval, int latency, double timeout) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putShort((short) (Math.round(minInterval) / 1.25));
        buffer.putShort((short) (Math.round(maxInterval) / 1.25));
        buffer.putShort((short) latency);
        buffer.putShort((short) (Math.round(timeout) / 10));

        writeCharacteristic(UUID_SERVICE_SETTING, UUID_CHARACTERISTIC_CONNECTION_PARAM_SETTING, buffer.array());
    }

    @Override
    protected BluetoothDataHandler createBluetoothDataHandler() {
        return new LocalBluetoothDataHandler();
    }

    private class LocalBluetoothDataHandler extends BluetoothDataHandler {

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            //notify方式返回来的数据

            UUID uuid = characteristic.getUuid();
            byte[] data = characteristic.getValue();

            Log.d(TAG, "onCharacteristicChanged() called with: " + "uuid = [" + uuid.toString() + "], data = [" + Arrays.toString(data) + "]");

            if (UUID_CHARACTERISTIC_MOTION_RECOGNIZER.equals(uuid)) {
                if (mSmartMotionDataCallback != null) {
                    mSmartMotionDataCallback.getMotionRecognizeData(data);
                }
            } else if (UUID_CHARACTERISTIC_MOTION_CONTINUITY.equals(uuid)) {
                if (mSmartMotionDataCallback != null) {
                    mSmartMotionDataCallback.getMotionContinuityData(data);
                }
            } else if (UUID_CHARACTERISTIC_MOTION_STOP.equals(uuid)) {
                if (mSmartMotionDataCallback != null) {
                    mSmartMotionDataCallback.getMotionStopData(data);
                }
            } else if (UUID_CHARACTERISTIC_FFF6.equals(uuid)) {
                convertActionParam(data);
            } else if (UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_STATUS.equals(uuid)) {
                convertFirmwareUpdateState(data);
            } else if (UUID_CHARACTERISTIC_PEDOMETER_STATUS.equals(uuid)) {
                if (mSmartPedometerDataCallback != null) {
                    mSmartPedometerDataCallback.getPedometerStatusData(data);
                }
            } else if (UUID_CHARACTERISTIC_PEDOMETER_RECORD_DATA.equals(uuid)) {
                convertPedometerRecordData(data);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            // write方式返回的处理结果
            if (status == BluetoothGatt.GATT_SUCCESS) {
                UUID uuid = characteristic.getUuid();
                byte[] value = characteristic.getValue();
                Log.d(TAG, name + " succeeded to write characteristic [" + uuid + "] value [" +
                        Arrays.toString(value) + "]");

                if (UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_DATA.equals(uuid)) {
                    if (mSmartSystemDataCallback != null) {
                        mSmartSystemDataCallback.onUploadFirmwareSuccess();
                    }
                }
            } else {
                //写入成功
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //read方式返回的处理结果

            Log.d(TAG, "onCharacteristicRead() called with: " + "gatt = [" + gatt + "], characteristic = [" + characteristic + "], status = [" + status + "]");

            UUID uuid = characteristic.getUuid();
            byte[] data = characteristic.getValue();

            if (status == BluetoothGatt.GATT_SUCCESS) {
                //读取成功
                if (UUID_CHARACTERISTIC_CONNECTION_PARAM.equals(uuid)) {
                    Log.d(TAG, "UUID_CHARACTERISTIC_CONNECTION_PARAM data = [" + Arrays.toString(data) + "]");
                    //convertConnectionParam(data);
//                    if (mSmartSystemDataCallback != null) {
//                        mSmartSystemDataCallback.getConnectionParamData(data);
//                    }
                } else if (UUID_CHARACTERISTIC_PEDOMETER_RECORD_STATUS.equals(uuid)) {
                    Log.d(TAG, "UUID_CHARACTERISTIC_PEDOMETER_RECORD_STATUS data = [" + Arrays.toString(data) + "]");
                    convertPedometerRecordStatusData(data);
                } else if (UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_TIME.equals(uuid)) {
                    Log.d(TAG, "UUID_CHARACTERISTIC_FIRMWARE_UPGRADE_TIME data = [" + Arrays.toString(data) + "]");
                    convertFirmwareUpdateTimeData(data);
                } else if (UUID_CHARACTERISTIC_FIRMWARE_VERSION.equals(uuid)) {
                    convertFirmwareVersionData(data);
                }else if(UUID_CHARACTERISTIC_NUMBER_NAME.equals(uuid)){
                      convertModelNumberVersionData(data);
                }else if(UUID_CHARACTERISTIC_HARDWARE_VERSION.equals(uuid)){
                    convertHardwareVersionData(data);
                }else if(UUID_CHARACTERISTIC_SOFTWARE_VERSION.equals(uuid)){
                    convertSoftwareVersionData(data);
                }
            } else {
                //读取失败
                Log.d(TAG, "onCharacteristicRead() fail");
            }
        }
    }

    private void convertFirmwareUpdateState(byte[] rawData) {
        ByteBuffer buffer = ByteBuffer.wrap(rawData);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        byte state = buffer.get();
        int counter = buffer.getInt();

        if (mSmartSystemDataCallback != null) {
            mSmartSystemDataCallback.onReceiveFirmwareUpgradeState(state, counter);
        }
    }

    /**
     * 解析固件版本
     *
     * @param data
     */
    private void convertFirmwareVersionData(byte[] data) {
        if (data == null) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        String version = null;
        try {
            version = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (version != null) {
           // version = version.substring(0, version.length() - 1);
            mContext.sendBroadcast(new Intent(ACTION_DEVICE_VERSION).putExtra(EXTRA_DATA, version));
            Log.d(TAG, "convertFirmwareVersionData() called with: " + "version = [" + version + "]");
        } else {
            Log.e(TAG, "convertFirmwareVersionData: fail version is null !");
        }
    }

    /**
     * 解析设备名称
     *
     * @param data
     */
    private void convertModelNumberVersionData(byte[] data) {
        if (data == null) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        String version = null;
        try {
            version = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (version != null) {
            // version = version.substring(0, version.length() - 1);
            mContext.sendBroadcast(new Intent(ACTION_DEVICE_MODEL_NAME).putExtra(EXTRA_DATA, version));
            Log.d(TAG, "convertModelNameData() called with: " + "version = [" + version + "]");
        } else {
            Log.e(TAG, "convertModelNameData: fail version is null !");
        }
    }

    /**
     * 解析软件版本
     *
     * @param data
     */
    private void convertSoftwareVersionData(byte[] data) {
        if (data == null) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        String version = null;
        try {
            version = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (version != null) {
            // version = version.substring(0, version.length() - 1);
            mContext.sendBroadcast(new Intent(ACTION_DEVICE_SOFT_VERSION).putExtra(EXTRA_DATA, version));
            Log.d(TAG, "convertSoftwareVersionData() called with: " + "version = [" + version + "]");
        } else {
            Log.e(TAG, "convertSoftwareVersionData: fail version is null !");
        }
    }

    /**
     * 解析硬件版本
     *
     * @param data
     */
    private void convertHardwareVersionData(byte[] data) {
        if (data == null) {
            return;
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        String version = null;
        try {
            version = new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (version != null) {
            // version = version.substring(0, version.length() - 1);
            mContext.sendBroadcast(new Intent(ACTION_DEVICE_HARD_VERSION).putExtra(EXTRA_DATA, version));
            Log.d(TAG, "convertHardVersionData() called with: " + "version = [" + version + "]");
        } else {
            Log.e(TAG, "convertHardVersionData: fail version is null !");
        }
    }
    /**
     * Parse connection param
     */
    private void convertConnectionParam(byte[] data) {


    }
    private void convertActionParam(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
       // Log.e(TAG, Arrays.toString(data));
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        short date = buffer.getShort();
        int time = buffer.getInt();
        short acc_x =  buffer.getShort();
        short acc_y =  buffer.getShort();
        short acc_z =  buffer.getShort();
        short ang_x =  buffer.getShort();
        short ang_y =  buffer.getShort();
        short ang_z =  buffer.getShort();
        byte action =  buffer.get();
        byte crc =  buffer.get();
        Log.d(TAG, "date=" + date + ",time=" + time + ",acc_x=" + acc_x + ",accy=" + acc_y + ",accz=" + acc_z + ",angx=" + ang_x + ",angy=" + ang_y + "," +
                "angz=" + ang_z + ",action=" + action + ",crc=" + crc);
        if (mSmartPedometerDataCallback != null) {
            mSmartPedometerDataCallback.getActionRecordData(data,action);
        }
    }

    /**
     * 解析计步数据的状态值，开始id和结束id
     *
     * @param data
     */
    private void convertPedometerRecordStatusData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int startId = buffer.getInt();
        int endId   = buffer.getInt();

        if (mSmartPedometerDataCallback != null) {
            mSmartPedometerDataCallback.getPedometerRecordStatusData(startId, endId);
        }

        Log.d(TAG, "convertPedometerRecordStatusData() called with: " + "startId = [" + startId + "]" + ",endId = [" + endId + "]");
    }

    /**
     * 解析计步数据的记录数据
     *
     * @param data
     */
    private synchronized void convertPedometerRecordData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int   id        = buffer.getInt();
        int   time      = buffer.getInt();
        short seconds   = buffer.getShort();
        short walkSteps = buffer.getShort();
        short runSteps  = buffer.getShort();
        short xActivity = buffer.getShort();
        short yActivity = buffer.getShort();
        short zActivity = buffer.getShort();

        PedometerRecordData pedometerRecordData = new PedometerRecordData(id, time, seconds, walkSteps, runSteps, xActivity, yActivity, zActivity);
        Log.e("SennoSmart","id="+id+",walksteps="+walkSteps+",runStep="+runSteps+" time = "+time+", scconds="+seconds);

        if (mSmartPedometerDataCallback != null) {
           // mSmartPedometerDataCallback.getPedometerRecordData(pedometerRecordData);
        } else {
            Log.e(TAG, "mSmartPedometerDataCallback is null ");
        }
    }

    /**
     * 解析固件升级时间
     *
     * @param data
     */
    private void convertFirmwareUpdateTimeData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int timestamp = buffer.getInt();

        Log.d(TAG, "convertFirmwareUpdateTimeData() called with: " + "time = [" + timestamp + "]");
        if (mSmartSystemDataCallback != null) {
            mSmartSystemDataCallback.onReceiveFirmwareUpgradeTime(timestamp);
        }
    }

    /**
     * 设置系统属性的数据回调接口
     *
     * @param smartSystemDataCallback
     */
    public void setSennoSmartSystemDataCallback(SmartSystemDataCallback smartSystemDataCallback) {
        this.mSmartSystemDataCallback = smartSystemDataCallback;
    }

    /**
     * 设置计步数据的回调接口
     *
     * @param smartPedometerDataCallback
     */
    public void setSennoSmartPedometerDataCallback(SmartPedometerDataCallback smartPedometerDataCallback) {
        this.mSmartPedometerDataCallback = smartPedometerDataCallback;
    }
}
