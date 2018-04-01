package com.android.liyun.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.liyun.Constant;
import com.android.liyun.base.ConnectStatusManager;
import com.android.liyun.bean.ConnectionTimeBean;
import com.android.liyun.listener.BleScanCallback;
import com.android.liyun.utils.SharedPreferencesUtil;
import com.android.liyun.utils.TimeStampUtil;
import com.android.liyun.utils.UIUtils;
import com.iflytek.speech.SynthesizerPlayer;
import com.liyun.blelibrary.BluetoothLeDevice;
import com.liyun.blelibrary.SennoSmart;
import com.liyun.blelibrary.listener.SmartPedometerDataCallback;
import com.liyun.blelibrary.listener.SmartSystemDataCallback;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author hzx
 * @create 2018-03-18 14:24.
 */

public class BleService extends Service{

    private static final String TAG = "BleService";

    public static final String ACTION_NOT_SUPPORT_BLUETOOTH_LE = "com.sennotech.kisscat.service.InsoleXBleService.ACTION_NOT_SUPPORT_BLUETOOTH_LE";
    public static final String ACTION_NOT_SUPPORT_BLUETOOTH = "com.sennotech.kisscat.service.InsoleXBleService.ACTION_NOT_SUPPORT_BLUETOOTH";
//    public static final String ACTION_ENABLE_BLUETOOTH         = "com.sennotech.kisscat.service.InsoleXBleService.ACTION_ENABLE_BLUETOOTH";

    private static final int WHAT_CONNECT_TIMEOUT = 1001;
    private static final long DELAY_SCANNING_TIMEOUT = 15_000;

    private static final int FACTORY_ID = 0x5453;

    private BluetoothAdapter mBluetoothAdapter;

    private final LocalLeScanCallback mLocalLeScanCallback = new LocalLeScanCallback();
    private static final String APPID = "appid=519328ab";
    //自定义蓝牙扫描回调接口
    private BleScanCallback mBleScanCallback;

    private SennoSmart mSennoSmart;

    private boolean mScanning = false;
    private BleServiceReceiver receiver;
    private Realm mRealm;
    private String current = "";
    private long timeMillis;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
        return new LocalBinder(this);
    }
    public static class LocalBinder extends Binder {

        private final WeakReference<BleService> mmHost;

        public LocalBinder(BleService host) {
            this.mmHost = new WeakReference<>(host);
        }

        public BleService getService() {
            return mmHost.get();
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mRealm = Realm.getDefaultInstance();
        registerBroadrecevicer();
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();

        //判断设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            sendBroadcast(new Intent(ACTION_NOT_SUPPORT_BLUETOOTH));
            return;
        }

        //判断设备是否支持蓝牙4.0
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            sendBroadcast(new Intent(ACTION_NOT_SUPPORT_BLUETOOTH_LE));
        }
    }

    @Override
    public void onDestroy() {
        //关闭蓝牙扫描
        stopScanning();
        //断开已经连接设备
        disconnect();
        //关闭蓝牙释放资源
        close();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        mRealm.close();
        super.onDestroy();
    }

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CONNECT_TIMEOUT:      //超时停止扫描
                    Toast.makeText(BleService.this, "未发现设备", Toast.LENGTH_SHORT)
                            .show();
                    stopScanning();
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    private class LocalLeScanCallback implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {
            Log.d(TAG, "onLeScan() called with: " + "bluetoothDevice = [" + bluetoothDevice.getName() + "], rssi = [" + rssi + "], scanRecord = [" + scanRecord + "]");

            if (bluetoothDevice == null) {
                return;
            }
            //发现设备接口回调，只为绑定设备而用
            if (mBleScanCallback != null) {
                mBleScanCallback.discoverDevice(bluetoothDevice, rssi);
            }


            String name = SharedPreferencesUtil.loadDeviceParams(BleService.this, SharedPreferencesUtil.KEY_DEVICE_NAME);
            String sn = SharedPreferencesUtil.loadDeviceParams(BleService.this, SharedPreferencesUtil.KEY_DEVICE_SERIAL_NUMBER);

            if (name != null && sn != null) {
                //找到测试目标设备
                if (name.equals(bluetoothDevice.getName()) && sn.equals(bluetoothDevice.getAddress())) {
                    if (mBleScanCallback != null) {
                        mBleScanCallback.discoverBindDevice(bluetoothDevice);
                    }
                }
            }
        }
    }

    /**
     * 通过传入目标设备进行生产单例类SennoSmart进行连接
     *
     * @param bluetoothDevice
     */
    public void connectDevice(BluetoothDevice bluetoothDevice) {
        mSennoSmart = new SennoSmart(BleService.this, bluetoothDevice, bluetoothDevice.getName());
        mSennoSmart.connect();
    }

    /**
     * 扫描蓝牙设备的准备检测
     */
    public void checkBleBeforeScan(BleScanCallback bleScanCallback) {
        this.mBleScanCallback = bleScanCallback;

        if (!mBleScanCallback.checkBluetoothPermission()) return;

        // 如果蓝牙未打开，提示手动开启蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            mBleScanCallback.enableBluetooth();
            return;
        }

        startToScan();
    }

    /**
     * 真正开始扫描蓝牙设备
     */
    public void startToScan() {
        if (!mScanning) {
            //开始扫描
            mScanning = mBluetoothAdapter.startLeScan(mLocalLeScanCallback);
            //取消之前的可能存在的超时等待 重新设置扫描超时等待，等待固定秒数停止扫描蓝牙设备
            mHandler.removeMessages(WHAT_CONNECT_TIMEOUT);
            mHandler.sendEmptyMessageDelayed(WHAT_CONNECT_TIMEOUT, DELAY_SCANNING_TIMEOUT);

            mBleScanCallback.startScanning();
        } else {
            Log.w(TAG, "bluetooth had been scanning");
        }
    }

    /**
     * 停止扫描
     */
    public synchronized void stopScanning() {
        if (mScanning) {
            //取消超时等待
            mHandler.removeMessages(WHAT_CONNECT_TIMEOUT);
            //停止扫描
            if (mBluetoothAdapter != null) mBluetoothAdapter.stopLeScan(mLocalLeScanCallback);

            mScanning = false;

            mBleScanCallback.stopScanning();
        } else {
            Log.w(TAG, "bluetooth scanning had been stopped");
        }
    }

    /**
     * 从手机获取时间同步到蓝牙设备
     */
    public void synchronizeTimeStamp() {
        if (mSennoSmart != null) {
            mSennoSmart.synchronizeTimeStamp();
        } else {
            Log.w(TAG, "synchronizeTimeStamp: mSennoSmart is null");
        }
    }


    /**
     * 获取连接参数的notify通道控制
     *
     * @param control
     */
    public void notifyConnectionParam(boolean control) {
        if (mSennoSmart != null) {
            mSennoSmart.notifyConnectionParam(control);
        } else {
            Log.w(TAG, "notifyConnectionParam fail mSennoSmart is null");
        }
    }

    /**
     * read方式读取一次连接参数
     */
    public void readConnectionParam() {
        if (mSennoSmart != null) {
            mSennoSmart.readConnectionParam();
        } else {
            Log.w(TAG, "readConnectionParam fail mSennoSmart is null");
        }
    }

    /**
     * write方式更新连接参数
     *
     * @param minInterval
     * @param maxInterval
     * @param latency
     * @param timeout
     */
    public void updateConnectionSetting(double minInterval, double maxInterval, int latency, double timeout) {
        if (mSennoSmart != null) {
            mSennoSmart.updateConnectionSetting(minInterval, maxInterval, latency, timeout);
        } else {
            Log.w(TAG, "updateConnectionSetting fail mSennoSmart is null");
        }
    }

    /**
     * 激活固件更新
     *
     * @param length
     */
    public void activeFirmwareUpgrade(int length) {
        if (mSennoSmart != null) {
            mSennoSmart.activeUpgrade(length);
        } else {
            Log.w(TAG, "activeFirmwareUpgrade fail mSennoSmart is null");
        }
    }

    public void enableFirmwareUpgrade() {
        if (mSennoSmart != null) {
            mSennoSmart.enableUpgrade();
        } else {
            Log.w(TAG, "activeFirmwareUpgrade fail mSennoSmart is null");
        }
    }

    /**
     * 向蓝牙芯片发送更新固件数据
     *
     * @param address
     * @param data
     */
    public void sendFirmwareUpgradeData(int address, byte[] data) {
        if (mSennoSmart != null) {
            mSennoSmart.writeFirmwareUpgradeData(address, data);
        } else {
            Log.w(TAG, "sendFirmwareUpgradeData fail mSennoSmart is null");
        }
    }

    /**
     * 获取固件更新状态的notify通道控制
     *
     * @param control
     */
    public void listenFirmwareUpgradeState(boolean control) {
        if (mSennoSmart != null) {
            mSennoSmart.notifyFirmwareUpgradeStatus(control);
        } else {
            Log.w(TAG, "sendFirmwareUpgradeData fail mSennoSmart is null");
        }
    }


    /**
     * 获取事实计步状态数据的notify通道控制
     *
     * @param control
     */
    public void notifyPedometerStatus(boolean control) {
        if (mSennoSmart != null) {
            mSennoSmart.notifyPedometerStatus(control);
        } else {
            Log.w(TAG, "notifyPedometerStatus fail mSennoSmart is null");
        }
    }

    public void getPedometerRecordStatus() {
        if (mSennoSmart != null) {
            mSennoSmart.readPedometerRecordStatus();
        } else {
            Log.w(TAG, "getPedometerRecordStatus fail mSennoSmart is null");
        }
    }

    public void startSynchronizePedometerRecordData(int startId) {
        if (mSennoSmart != null) {
            mSennoSmart.notifyPedometerRecordData(true);
            mSennoSmart.startSynchronizePedometerRecordData(startId);
        } else {
            Log.w(TAG, "getPedometerRecordControl fail mSennoSmart is null");
        }
    }

    /**
     * 获取固件的版本号
     */
    public void readFirmwareVersion() {
        if (mSennoSmart != null) {
            mSennoSmart.readFirmwareVersion();
        } else {
            Log.w(TAG, "readFirmwareVersion fail mSennoSmart is null");
        }
    }

    /**
     * 获取设备名称
     */
    public void readModelNumber() {
        if (mSennoSmart != null) {
            mSennoSmart.readModelNumber();
        } else {
            Log.w(TAG, "readFirmwareVersion fail mSennoSmart is null");
        }
    }

    /**
     * 获取硬件的版本号
     */
    public void readHardwareVersion() {
        if (mSennoSmart != null) {
            mSennoSmart.readHardwareVersion();
        } else {
            Log.w(TAG, "readFirmwareVersion fail mSennoSmart is null");
        }
    }

    /**
     * 获取软件的版本号
     */
    public void readSoftVersion() {
        if (mSennoSmart != null) {
            mSennoSmart.readSoftVersion();
        } else {
            Log.w(TAG, "readFirmwareVersion fail mSennoSmart is null");
        }
    }

    /**
     * 获取固件的更新时间
     */
    public void readFirmwareUpdateTime() {
        if (mSennoSmart != null) {
            mSennoSmart.readFirmwareUpdateTime();
        } else {
            Log.w(TAG, "readFirmwareUpdateTime fail mSennoSmart is null");
        }
    }

    public void disconnect() {
        if (mSennoSmart != null) {
            Log.d(TAG, "disconnect sennosmart");
            mSennoSmart.disconnect();
        } else {
            Log.w(TAG, "close fail mSennoSmart is null");
        }
    }

    public void close() {
        if (mSennoSmart != null) {
            Log.d(TAG, "close sennosmart");
            mSennoSmart.close();
            mSennoSmart = null;
        } else {
            Log.w(TAG, "close fail mSennoSmart is null");
        }
    }

    public void setSennoSmartSystemDataCallback(SmartSystemDataCallback smartSystemDataCallback) {
        if (mSennoSmart != null) {
            mSennoSmart.setSennoSmartSystemDataCallback(smartSystemDataCallback);
        } else {
            Log.w(TAG, "setSennoSmartSystemDataCallback fail mSennoSmart is null");
        }
    }

    public void setSennoSmartPedometerDataCallback(SmartPedometerDataCallback smartPedometerDataCallback) {
        if (mSennoSmart != null) {
            mSennoSmart.setSennoSmartPedometerDataCallback(smartPedometerDataCallback);
        } else {
            Log.w(TAG, "setSennoSmartPedometerDataCallback fail mSennoSmart is null");
        }
    }


    private void registerBroadrecevicer() {
        receiver = new BleServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeDevice.ACTION_DISCONNECTED);
        filter.addAction(BluetoothLeDevice.ACTION_AUDIO_SPEED);
        registerReceiver(receiver, filter);
    }

    public class BleServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothLeDevice.ACTION_DISCONNECTED)) {
                onDeviceDisconnected();
            }else if (action.equals(BluetoothLeDevice.ACTION_AUDIO_SPEED)) {
                String mAction = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                byte b = Byte.parseByte(mAction);
                speedVoice(b);
            }
        }
    }

    private void speedVoice(byte action) {
        byte[] bytes = new byte[1];
        bytes[0] = action;
        final String s = bytesToHexString(bytes);

        if (s.equals("00")) {

        } else {
            if (s.equals(current)) {
                return;
            } else {
                Log.e("TAG", "进来s======"+s);
               /* mApi.driveVideo(DRIVEVIDEL, SPUtil.getString(UIUtils.getContext(), ConstValues.UID, ""),
                        SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, ""), Arrays.toString(data));*/
                System.out.println(s);
                switch (s) {
                    case "a1":
                        current = "a1";
                        timeMillis = System.currentTimeMillis();
                        sayCar("车头检测中，请等待");
                        break;
                    case "a2":
                        current = "a2";
                        sayCar("车头检测中，请直线行驶");
                        break;
                    case "00"://正常
                        sayCar("车头检测中，请等待");
                        break;
                    case "a3":
                        current = "a3";
                        sayCar("车头检测结束，请正常行驶");
                        break;
                    case "02":
                        current = "02";
                        sayCar("检测到加速");
                        break;
                    case "12":
                        current = "12";
                        sayCar("检测到急加速");
                        break;
                    case "03":
                        current = "03";
                        sayCar("检测到刹车");
                        break;
                    case "13":
                        current = "13";
                        sayCar("检测到急刹车");
                        break;
                    case "04":
                        current = "04";
                        sayCar("检测到左转");
                        break;
                    case "14":
                        current = "14";
                        sayCar("检测到急速左转");
                        break;
                    case "05":
                        current = "05";
                        sayCar("检测到右转");
                        break;
                    case "15":
                        current = "15";
                        sayCar("检测到急速右转");
                        break;
                    case "07":
                        current = "07";
                        sayCar("检测到左变道");
                        break;
                    case "08":
                        current = "08";
                        sayCar("检测到右变道");
                        break;
                    case "09":
                        current = "09";
                        sayCar("检测到掉头");
                        break;
                }
            }
        }
    }
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    private void sayCar(String action) {
        SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(UIUtils.getContext(), APPID);
        //设置语音朗读者，可以根据需要设置男女朗读，具体请看api文档和官方论坛
        player.setVoiceName("vivixiaoyan");//在此设置语音播报的人选例如：vivixiaoyan、vivixiaomei、vivixiaoqi
        player.playText(action, "ent=vivi21,bft=5", null);

    }

    //  @Receiver(actions = BluetoothLeDevice.ACTION_DISCONNECTED)
    protected void onDeviceDisconnected() {
        //service是贯穿整个app的，当收到断开连接的广播，及时更新当前设备的连接状态
        ConnectStatusManager.changeConnectionStatus(false);
        String disconnectTime = TimeStampUtil.unixTimeStamp2Datems(String.valueOf(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
        double energyNum = TimeStampUtil.calculateEnergyNum(Constant.START_CONNECT_TIME, disconnectTime);
        DecimalFormat df = new DecimalFormat("#0.00");
        String energyNumFormat = df.format(energyNum);
        saveData(false, Double.parseDouble(energyNumFormat), disconnectTime);
        Log.e("time", "disconnecttime=" + disconnectTime + ",energyNum=" + energyNum + "--energyNumFormat=" + Double.parseDouble(energyNumFormat));
    }

    private void saveData(final boolean isUpload, final double num, final String disconnectTime) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                //TODO  disconnectTime这里把断开时候时间作为创建时间，也可以把开始时间作为
                ConnectionTimeBean cc = realm.createObject(ConnectionTimeBean.class, generateNewPrimaryKey());
                cc.setCreateTime(disconnectTime); //这里把断开链接
                cc.setUpload(isUpload);//TODO 没有上传服务器的 当你点击水滴就上传服务器，上传成功就更改为true
                cc.setEnergyNum(num);  //TODO 暂时算出来的水滴数量
            }
        });
    }

    //获取最大的PrimaryKey并加一
    private long generateNewPrimaryKey() {
        long primaryKey = 0;
        //必须排序, 否则last可能不是PrimaryKey最大的数据. findAll()查询出来的数据是乱序的
        RealmResults<ConnectionTimeBean> results = mRealm.where(ConnectionTimeBean.class).findAllSorted("id", Sort.ASCENDING);
        if (results != null && results.size() > 0) {
            ConnectionTimeBean last = results.last(); //根据id顺序排序后, last()取得的对象就是PrimaryKey的值最大的数据
            primaryKey = last.getId() + 1;
        }
        return primaryKey;
    }
}

