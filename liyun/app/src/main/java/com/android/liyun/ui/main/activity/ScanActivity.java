package com.android.liyun.ui.main.activity;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.Constant;
import com.android.liyun.R;
import com.android.liyun.adapter.DeviceAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.ConnectStatusManager;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.bean.Device;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.listener.BleScanCallback;
import com.android.liyun.service.BleService;
import com.android.liyun.ui.goods.AddAddressAct;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.SharedPreferencesUtil;
import com.android.liyun.utils.TimeStampUtil;
import com.android.liyun.utils.ToastUtils;
import com.android.liyun.utils.UIUtils;
import com.liyun.blelibrary.BluetoothLeDevice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author hzx
 * created at 2018/3/20 12:15
 */
public class ScanActivity extends BaseActivity implements ConnectStatusManager.StatusChangeCallback {
    @BindView(R.id.lv_device)
    ListView lvDevice;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.pb)
    ProgressBar pb;
    private long mLatestBackTimestamp = 0;
    //表示当前扫描状态的变量
    private boolean isScanning = false;
    private boolean isConnecting = false;
    private ArrayList<Device> devices = new ArrayList<>();
    private List<Device> devicesList = new ArrayList<>();
    private DeviceAdapter mDeviceAdapter;

    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 101;
    protected LiyunApp mLiYunApp;
    private BleService mSennoSmartBleService;
    private BroadcastReceiver receiver;

    @Override
    public int getLayoutId() {
        return R.layout.act_scan;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {

        super.onResume();
        toolbar.setTitle("扫描设备");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ConnectStatusManager.setStatusChangeCallback(ScanActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public void initView() {
        initview();
        initListview();
        registerBroadrecevicer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_add:
                if (!isScanning) {
                    scanDevice();
                } else {
                    mSennoSmartBleService.stopScanning();
                }
                break;
            default:
                break;
        }
        return true;

    }

    private void registerBroadrecevicer() {
        receiver = new IntenterBoradCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeDevice.ACTION_CONNECT_TIMEOUT);
        filter.addAction(BluetoothLeDevice.ACTION_SERVICES_DISCOVERED);
        registerReceiver(receiver, filter);
    }

    private ProgressDialog progressDialog;

    private void initListview() {
        mDeviceAdapter = new DeviceAdapter(this, devicesList);
        lvDevice.setAdapter(mDeviceAdapter);
        lvDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Device device = devicesList.get(position);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean b = SharedPreferencesUtil.saveDeviceParams(ScanActivity.this, device.getName(), device.getAddress());
                        if (b) {
                            ToastUtils.showToast("添加成功");
                            finish();
                        }
                    /*    mSennoSmartBleService.stopScanning();
                        mSennoSmartBleService.connectDevice(device.getBluetoothDevice());
                        isConnecting = true;
                        progressDialog = new ProgressDialog(ScanActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setMessage("正在连接中...");
                        progressDialog.show();*/
                    }
                });

            }
        });
    }

    private void initview() {
        mLiYunApp = LiyunApp.instance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSennoSmartBleService = mLiYunApp.getSennoSmartBleService();
                scanDevice();
            }
        }, 1000);
    }

    @OnClick({R.id.tv_scan})
    public void onClickScan() {
        if (!isScanning) {
            scanDevice();
        } else {
            mSennoSmartBleService.stopScanning();
        }
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
                if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanActivity.this, new String[]{Manifest
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
            final Device device = new Device();
            device.setBluetoothDevice(bluetoothDevice);
            device.setName(bluetoothDevice.getName());
            device.setRssi(rssi);
            device.setAddress(bluetoothDevice.getAddress());
            devices.add(device);
            devicesList = removeDuplicteUsers(devices);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceAdapter.updateListView(devicesList);
                }
            });
            //当发现设备后继续让蓝牙扫描5秒
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSennoSmartBleService.stopScanning();
                }
            }, 5000);
        }

        @Override
        public void discoverBindDevice(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void startScanning() {
            isScanning = true;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    devicesList.clear();
                    mDeviceAdapter.notifyDataSetChanged();
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void stopScanning() {
            isScanning = false;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.INVISIBLE);
                }
            });
        }
    };

    @Override
    public void changeStatus(boolean isConnected) {

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
      /*  if (mSennoSmartBleService != null) {
            mSennoSmartBleService.disconnect();
            mSennoSmartBleService.close();
        }
        stopService(new Intent(this, BleService.class));*/
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onDestroy();


    }

    public ArrayList<Device> removeDuplicteUsers(ArrayList<Device> userList) {
        Set<Device> s = new TreeSet<>(new Comparator<Device>() {

            @Override
            public int compare(Device o1, Device o2) {
                return o1.getAddress().compareTo(o2.getAddress());
            }

        });

        s.addAll(userList);
        return new ArrayList<>(s);
    }

    /**
     * 收到连接超时的广播
     */
    //   @Receiver(actions = BluetoothLeDevice.ACTION_CONNECT_TIMEOUT)
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
                startActivity(new Intent(ScanActivity.this, DeviceInfoActivity.class));
//                mSennoSmartBleService.synchronizeTimeStamp();
//              //  mSennoSmartBleService.setSennoSmartPedometerDataCallback(mSennoSmartPedometerDataCallback);
//                mSennoSmartBleService.notifyPedometerStatus(true);
//                mSennoSmartBleService.getPedometerRecordStatus();

                //startSynchronizePedometerRecordData();
            }
        }, 1000);
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
}
