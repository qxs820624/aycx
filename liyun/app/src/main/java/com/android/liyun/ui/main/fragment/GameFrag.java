package com.android.liyun.ui.main.fragment;

import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.ui.main.activity.ScanActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class GameFrag extends BaseFragment {
    @BindView(R.id.tv_connect)
    TextView tvConnect;
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

    @OnClick(R.id.tv_connect)
    public void onClick(){
        startActivity(ScanActivity.class);
    }
}
