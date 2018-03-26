package com.android.liyun.ui.manager;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;

import butterknife.BindView;

public class ManagementAct extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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

   /* @OnClick(R.id.txt_confirm_submit)
    public void onClick() {
    }*/

}
