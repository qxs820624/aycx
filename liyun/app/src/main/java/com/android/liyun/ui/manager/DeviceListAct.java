package com.android.liyun.ui.manager;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.liyun.Constant;
import com.android.liyun.R;
import com.android.liyun.adapter.DeviceListAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.ConnectStatusManager;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.bean.Device;
import com.android.liyun.listener.BleScanCallback;
import com.android.liyun.service.BleService;
import com.android.liyun.ui.main.activity.ScanActivity;
import com.android.liyun.utils.SharedPreferencesUtil;
import com.android.liyun.utils.TimeStampUtil;
import com.android.liyun.utils.UIUtils;
import com.liyun.blelibrary.BluetoothLeDevice;

import java.util.ArrayList;

import butterknife.BindView;

public class DeviceListAct extends BaseActivity implements ConnectStatusManager.StatusChangeCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listview)
    ListView listview;
    private static final String TAG = DeviceListAct.class.getSimpleName();
    protected LiyunApp mLiYunApp;
    private DeviceListAdapter deviceListAdapter;
    private BleService mSennoSmartBleService;
    private BroadcastReceiver receiver;
    private boolean isScanning = false;
    private boolean isConnecting = false;
    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 101;
    @Override
    public int getLayoutId() {
        return R.layout.act_devicelist;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectStatusManager.setStatusChangeCallback(DeviceListAct.this);
        toolbar.setTitle("设备列表");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initDevice();

    }

    private void initDevice() {
        ArrayList<Device> devicesList = new ArrayList<>();
        Device Device = new Device();
        Device.setName(SharedPreferencesUtil.loadDeviceParams(this,SharedPreferencesUtil.KEY_DEVICE_NAME));
        Device.setAddress(SharedPreferencesUtil.loadDeviceParams(this,SharedPreferencesUtil.KEY_DEVICE_SERIAL_NUMBER));
        devicesList.add(Device);
        deviceListAdapter = new DeviceListAdapter(UIUtils.getContext(), devicesList, R.layout.item_add_device, new DeviceListAdapter.CallBack() {
            @Override
            public void click(View view) {
                Log.e("TAG", "DDD----------");
            }
        });
        listview.setAdapter(deviceListAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if(!ConnectStatusManager.isConnected()){
                   if (!isScanning) {
                       scanDevice();
                   } else {
                       mSennoSmartBleService.stopScanning();
                   }
               }else {
                   startActivity(ManagementAct.class);
               }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_ble, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_addble:
                startActivity(ScanActivity.class);
                break;
        }
        return true;
    }

    @Override
    public void initView() {
        mLiYunApp = LiyunApp.instance();
        mSennoSmartBleService = mLiYunApp.getSennoSmartBleService();
        registerBroadrecevicer();
    }
    private void registerBroadrecevicer() {
        receiver = new IntenterBoradCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeDevice.ACTION_CONNECT_TIMEOUT);
        filter.addAction(BluetoothLeDevice.ACTION_SERVICES_DISCOVERED);
        registerReceiver(receiver, filter);
    }
    private void scanDevice() {
        if (mSennoSmartBleService != null) {
            mSennoSmartBleService.checkBleBeforeScan(mBleScanCallback);
        } else {
            Log.e("TAG", "ble==null");
        }

    }
    private final BleScanCallback mBleScanCallback = new BleScanCallback() {

        @Override
        public boolean checkBluetoothPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(DeviceListAct.this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeviceListAct.this, new String[]{Manifest
                                    .permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_COARSE_LOCATION);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }

        @Override
        public void enableBluetooth() {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        @Override
        public void discoverDevice(final BluetoothDevice bluetoothDevice, int rssi) {

        }

        @Override
        public void discoverBindDevice(final BluetoothDevice bluetoothDevice) {
            Log.d(TAG, "discoverBindDevice() called with: " + "bluetoothDevice = [" + bluetoothDevice.getName() + "]");
            if (!isConnecting) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSennoSmartBleService.connectDevice(bluetoothDevice);
                        isConnecting = true;
                        progressDialog = new ProgressDialog(DeviceListAct.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("正在连接中...");
                        progressDialog.show();
                    }
                });
            }
        }

        @Override
        public void startScanning() {
            isScanning = true;
        }

        @Override
        public void stopScanning() {
            isScanning = false;
        }
    };
    private ProgressDialog progressDialog;

    @Override
    public void changeStatus(boolean isConnected) {

    }

    public class IntenterBoradCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothLeDevice.ACTION_CONNECT_TIMEOUT)) {
                onReceiverConnectTimeout();
            } else if (action.equals(BluetoothLeDevice.ACTION_SERVICES_DISCOVERED)) {
                onReceiverDiscoveredService();
            }
        }
    }
    /**
     * 收到连接超时的广播
     */
    protected void onReceiverConnectTimeout() {

        // TODO: 2016/8/13  连接超时的处理
        mSennoSmartBleService.stopScanning();
        isConnecting = false;   //改变状态为非连接状态
        ConnectStatusManager.changeConnectionStatus(false);
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        Toast.makeText(this, "连接超时", Toast.LENGTH_LONG).show();
    }
    /**
     * 收到发现服务的广播
     */
    // @Receiver(actions = BluetoothLeDevice.ACTION_SERVICES_DISCOVERED)
    protected void onReceiverDiscoveredService() {
        ConnectStatusManager.changeConnectionStatus(true);
        Constant.START_CONNECT_TIME = TimeStampUtil.unixTimeStamp2Datems(String.valueOf(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
        Log.e("time", Constant.START_CONNECT_TIME);
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        Toast.makeText(this, "设备连接成功", Toast.LENGTH_LONG).show();

        mSennoSmartBleService.stopScanning();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnecting = false;   //改变状态为非连接状态
                startActivity(new Intent(DeviceListAct.this, ManagementAct.class));
               // mSennoSmartBleService.setSennoSmartPedometerDataCallback(this);
                mSennoSmartBleService.notifyConnectionParam(true);
//                mSennoSmartBleService.synchronizeTimeStamp();
//              //  mSennoSmartBleService.setSennoSmartPedometerDataCallback(mSennoSmartPedometerDataCallback);
//                mSennoSmartBleService.notifyPedometerStatus(true);
//                mSennoSmartBleService.getPedometerRecordStatus();

                //startSynchronizePedometerRecordData();
            }
        }, 1000);
    }
    @Override
    protected void onPause() {
        if (mSennoSmartBleService != null) {
            //当页面失焦的时候停止蓝牙扫描
            mSennoSmartBleService.stopScanning();
        }
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
      super.onDestroy();
    }
}
