package com.android.liyun.ui.main.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.ui.main.activity.MainActivity;
import com.android.liyun.ui.main.activity.ScanActivity;
import com.iflytek.speech.SynthesizerPlayer;

import butterknife.BindView;
import butterknife.OnClick;

public class GameFrag extends BaseFragment {
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    private static final String APPID = "appid=519328ab";
    private MainActivity mMainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // TODO 这里不应该强转成MainActivity
        this.mMainActivity = (MainActivity) activity;
    }


    @Override
    protected int getLayoutResource() {

        return R.layout.fra_game;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.btn_voice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice:
                SynthesizerPlayer player = SynthesizerPlayer.createSynthesizerPlayer(mMainActivity, APPID);
                //设置语音朗读者，可以根据需要设置男女朗读，具体请看api文档和官方论坛
                player.setVoiceName("vivixiaoyan");//在此设置语音播报的人选例如：vivixiaoyan、vivixiaomei、vivixiaoqi
                player.playText("车辆正在启动", "ent=vivi21,bft=5", null);
                break;
        }
    }

    @OnClick(R.id.tv_connect)
    public void onClick() {
        startActivity(ScanActivity.class);
    }
}
