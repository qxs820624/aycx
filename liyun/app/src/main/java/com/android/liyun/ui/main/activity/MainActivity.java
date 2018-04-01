package com.android.liyun.ui.main.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.liyun.Constant;
import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.ConnectStatusManager;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.listener.BleScanCallback;
import com.android.liyun.service.BleService;
import com.android.liyun.ui.login.LoginAct;
import com.android.liyun.ui.main.fragment.ForumFrag;
import com.android.liyun.ui.main.fragment.GameFrag;
import com.android.liyun.ui.main.fragment.HomeFrag;
import com.android.liyun.ui.main.fragment.MallFrag;
import com.android.liyun.ui.main.fragment.MyFrag;
import com.android.liyun.utils.BottomNavigationViewHelper;
import com.android.liyun.utils.SharedPreferencesUtil;
import com.android.liyun.utils.StatusBarUtil;
import com.android.liyun.utils.TimeStampUtil;
import com.android.liyun.widget.NoSlidingViewPaper;
import com.liyun.blelibrary.BluetoothLeDevice;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ConnectStatusManager.StatusChangeCallback {

    //值唯一即可,这是为了返回做标识使用
    private final int REQUEST_SETTING = 10;
    private static final int EXIT_APP_DELAY = 1000;
    private static final int REQUEST_ENABLE_BT = 1001;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1001;
    private static final int PERMISSION_REQUEST_WRITE_SDCARD = 1002;
    private long lastTime = 0;
    private boolean isConnecting = false;
    private boolean isScanning   = false;
    @BindView(R.id.container)
    RelativeLayout relativeLayout;
    public BottomNavigationView navigation;
    private ArrayList<Fragment> fgLists;
    private NoSlidingViewPaper mViewPager;
    private LiyunApp mLiYunApp;
    private BleService mSennoSmartBleService;
    private Realm mRealm;

    @Override
    public int getLayoutId() {
        return R.layout.act_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mRealm = Realm.getDefaultInstance();
        /*初始化显示内容*/
        mViewPager = (NoSlidingViewPaper) findViewById(R.id.vp_main_container);
        fgLists = new ArrayList<>(5);
        fgLists.add(new HomeFrag());
        fgLists.add(new GameFrag());
        fgLists.add(new MallFrag());
        fgLists.add(new ForumFrag());
        fgLists.add(new MyFrag());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fgLists.get(position);
            }

            @Override
            public int getCount() {
                return fgLists.size();
            }
        };
        mViewPager.setOffscreenPageLimit(3); //预加载剩下两页
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        initDevice();
    }

    private void initDevice() {
        mLiYunApp = LiyunApp.instance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSennoSmartBleService = mLiYunApp.getSennoSmartBleService();
                scanDevice();
            }
        }, 1000);
    }
    /**
     * 扫描设备前进行判断是否已经绑定设备
     * 若没有绑定则弹窗指引用户前去绑定设备
     */
    public  void scanDevice() {
        Log.e("TAG", "执行scan");
        if (hadBindDevice()) {
            if (mSennoSmartBleService != null) {
                mSennoSmartBleService.checkBleBeforeScan(mBleScanCallback);
            }
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("当前无绑定设备，请先绑定设备！")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(ScanActivity.class);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        }
    }
    /**
     * 检查是否已经绑定设备
     *
     * @return
     */
    private boolean hadBindDevice() {
        String name         = SharedPreferencesUtil.loadDeviceParams(MainActivity.this, SharedPreferencesUtil.KEY_DEVICE_NAME);
        String serialNumber = SharedPreferencesUtil.loadDeviceParams(MainActivity.this, SharedPreferencesUtil.KEY_DEVICE_SERIAL_NUMBER);
        return name != null && serialNumber != null;
    }
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this, null);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0, false);
                    return true;
                case R.id.navigation_managemoney:
                    mViewPager.setCurrentItem(1, false);
                    return true;
                case R.id.navigation_dashboard:
                    mViewPager.setCurrentItem(2, false);
                    return true;
                case R.id.navigation_notifications:
                    mViewPager.setCurrentItem(3, false);
                    return true;
                case R.id.navigation_my:
                    if (isLogin()) {
                        mViewPager.setCurrentItem(4, false);
                        return true;
                    } else {
                        startActivity(LoginAct.class);
                        return false;
                    }
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        ConnectStatusManager.setStatusChangeCallback(MainActivity.this);
        mLiYunApp = LiyunApp.instance();
        mSennoSmartBleService = mLiYunApp.getSennoSmartBleService();
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
        if (mSennoSmartBleService != null) {
            mSennoSmartBleService.disconnect();
            mSennoSmartBleService.close();
        }
        //关闭数据库
        mRealm.close();
        super.onDestroy();


        if (mSennoSmartBleService != null) {
            mSennoSmartBleService.disconnect();
            mSennoSmartBleService.close();
        }

    }
    private final BleScanCallback mBleScanCallback = new BleScanCallback() {

        @Override
        public boolean checkBluetoothPermission() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest
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
            if (!isConnecting) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSennoSmartBleService.connectDevice(bluetoothDevice);
                        isConnecting = true;
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - lastTime) > EXIT_APP_DELAY) {
                Snackbar.make(relativeLayout, getString(R.string.press_twice_exit), Snackbar.LENGTH_SHORT).setAction(R.string.exit_directly, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.super.onBackPressed();
                    }
                }).show();
                lastTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(true);
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        switch (position) {
//            case 0:
//                navigation.setSelectedItemId(R.id.navigation_home);
//                break;
//            case 1:
//                navigation.setSelectedItemId(R.id.navigation_managemoney);
//                break;
//            case 2:
//                navigation.setSelectedItemId(R.id.navigation_dashboard);
//                break;
//            case 3:
//                navigation.setSelectedItemId(R.id.navigation_notifications);
//            case 4:
//                navigation.setSelectedItemId(R.id.navigation_my);
//                break;
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void changeStatus(boolean isConnected) {

    }
    /**
     * 收到连接超时的广播
     */
    protected void onReceiverConnectTimeout() {

        // TODO: 2016/8/13  连接超时的处理
        mSennoSmartBleService.stopScanning();
        isConnecting = false;   //改变状态为非连接状态
        ConnectStatusManager.changeConnectionStatus(false);

        Toast.makeText(this, "连接超时", Toast.LENGTH_LONG).show();
    }
    protected void onReceiverDiscoveredService() {
        ConnectStatusManager.changeConnectionStatus(true);
        Constant.START_CONNECT_TIME = TimeStampUtil.unixTimeStamp2Datems(String.valueOf(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
        Log.e("time", Constant.START_CONNECT_TIME);
        Toast.makeText(this, "设备连接成功", Toast.LENGTH_LONG).show();

        mSennoSmartBleService.stopScanning();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isConnecting = false;   //改变状态为非连接状态
                mSennoSmartBleService.notifyConnectionParam(true);
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
