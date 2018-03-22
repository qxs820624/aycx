package com.android.liyun.ui.main.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.service.BleService;
import com.liyun.blelibrary.BluetoothLeDevice;
import com.liyun.blelibrary.listener.SmartPedometerDataCallback;

import java.util.Arrays;

import butterknife.BindView;

/**
*
*@author hzx
*created at 2018/3/20 12:15
*/
public class ShowDataActivity extends BaseActivity implements SmartPedometerDataCallback {
    @BindView(R.id.tv_ModelNumber)
    TextView tvModelNumber;
    @BindView(R.id.tv_FirmwareRevision)
    TextView tvFirmwareRevision;
    @BindView(R.id.tv_HardwareRevision)
    TextView tvHardwareRevision;
    @BindView(R.id.tv_SoftwareRevision)
    TextView tvSoftwareRevision;
    @BindView(R.id.tv_fff6)
    TextView tv_fff6;
    @BindView(R.id.tv_fff6data)
    TextView tv_fff6data;

    private BleService mSennoSmartBleService;
    private LiyunApp mLiyunApp;
    private BroadcastReceiver receiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_data;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mLiyunApp = LiyunApp.instance();
        mSennoSmartBleService = mLiyunApp.getSennoSmartBleService();
        initData();
        registerBroadrecevicer();
    }

    private void initData() {
        mSennoSmartBleService.setSennoSmartPedometerDataCallback(this);
        mSennoSmartBleService.notifyConnectionParam(true);
        mSennoSmartBleService.readFirmwareVersion();
        mSennoSmartBleService.readSoftVersion();
        mSennoSmartBleService.readHardwareVersion();
        mSennoSmartBleService.readModelNumber();
    }

    private void registerBroadrecevicer() {
        receiver = new IntenteBoradCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothLeDevice.ACTION_DEVICE_VERSION);
        filter.addAction(BluetoothLeDevice.ACTION_DEVICE_MODEL_NAME);
        filter.addAction(BluetoothLeDevice.ACTION_DEVICE_SOFT_VERSION);
        filter.addAction(BluetoothLeDevice.ACTION_DEVICE_HARD_VERSION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void getPedometerStatusData(byte[] data) {

    }

    @Override
    public void getPedometerRecordStatusData(int startId, int endId) {

    }

    @Override
    public void getActionRecordData(final byte[] data, final byte action) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_fff6.setText(action+"");
                tv_fff6data.setText(Arrays.toString(data));
            }
        });

    }

    public class IntenteBoradCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothLeDevice.ACTION_DEVICE_VERSION)) {
                String firmwareRevision = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvFirmwareRevision.setText(firmwareRevision);
            }else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_MODEL_NAME)) {
                String ModelName = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvModelNumber.setText(ModelName);
            }else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_SOFT_VERSION)) {
                String softVersion = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvSoftwareRevision.setText(softVersion);
            }else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_HARD_VERSION)) {
                String hardVersion = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvHardwareRevision.setText(hardVersion);
            }
        }
    }
}
