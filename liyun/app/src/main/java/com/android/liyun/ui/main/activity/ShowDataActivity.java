package com.android.liyun.ui.main.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.service.BleService;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.iflytek.speech.SynthesizerPlayer;
import com.liyun.blelibrary.BluetoothLeDevice;
import com.liyun.blelibrary.listener.SmartPedometerDataCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.android.liyun.http.RequestWhatI.DRIVEVIDEL;

/**
 * @author hzx
 *         created at 2018/3/20 12:15
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
    private static final String APPID = "appid=519328ab";
    private List<byte[]> arr = new ArrayList<>();

    private String current = "";
    private long timeMillis;

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_data;
    }

    @Override
    public void initPresenter() {
        mApi = new Api(handler, this);
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
        byte[] bytes = new byte[1];
        bytes[0] = action;
        String s = bytesToHexString(bytes);
        if (s.equals("00")) {

        } else {

            if (s.equals(current)) {
                return;
            } else {
                mApi.driveVideo(DRIVEVIDEL, SPUtil.getString(UIUtils.getContext(), ConstValues.UID, ""),
                        SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, ""), Arrays.toString(data));
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


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_fff6.setText(action + "");
                for (int i = 0; i < data.length; i++) {
                    if (data[18] != 0) {
                        arr.add(data);
                    }
                }


                tv_fff6data.setText(Arrays.toString(data));
//                System.out.println(Arrays.toString(data));


            }
        });

//        try {
//            Thread.sleep(2000);
//            System.out.println(Arrays.toString(data));
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    private void sayCar(String action) {
        SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(UIUtils.getContext(), APPID);
        //设置语音朗读者，可以根据需要设置男女朗读，具体请看api文档和官方论坛
        player.setVoiceName("vivixiaoyan");//在此设置语音播报的人选例如：vivixiaoyan、vivixiaomei、vivixiaoqi
        player.playText(action, "ent=vivi21,bft=5", null);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case DRIVEVIDEL:
                        BaseBen baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
    };

    public class IntenteBoradCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothLeDevice.ACTION_DEVICE_VERSION)) {
                String firmwareRevision = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvFirmwareRevision.setText(firmwareRevision);
            } else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_MODEL_NAME)) {
                String ModelName = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvModelNumber.setText(ModelName);
            } else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_SOFT_VERSION)) {
                String softVersion = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvSoftwareRevision.setText(softVersion);
            } else if (action.equals(BluetoothLeDevice.ACTION_DEVICE_HARD_VERSION)) {
                String hardVersion = intent.getStringExtra(BluetoothLeDevice.EXTRA_DATA);
                tvHardwareRevision.setText(hardVersion);
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
}
