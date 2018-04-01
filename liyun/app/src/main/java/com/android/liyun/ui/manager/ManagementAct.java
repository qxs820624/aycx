package com.android.liyun.ui.manager;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.ui.main.activity.DeviceInfoActivity;
import com.android.liyun.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ManagementAct extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rlyt_air_upgrade)
    RelativeLayout  rl_air_upgrade;
    @BindView(R.id.rlyt_memory_management)
    RelativeLayout  rl_memory_management;
    @BindView(R.id.rlyt_equipment_info)
    RelativeLayout  rl_equipment_info;
    @BindView(R.id.rlyt_history)
    RelativeLayout  rl_history;
    @Override
    public int getLayoutId() {
        return R.layout.act_equipment;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("设备管理");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initView() {
    }

    @OnClick({R.id.rlyt_history,R.id.rlyt_equipment_info,R.id.rlyt_memory_management,R.id.rlyt_air_upgrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlyt_history:

                break;
            case R.id.rlyt_equipment_info:
                 startActivity(DeviceInfoActivity.class);
                break;
            case R.id.rlyt_memory_management:

                break;
            case R.id.rlyt_air_upgrade:
                ToastUtils.showToast("已是最新版本");
                break;
        }
    }

}
