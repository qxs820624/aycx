package com.android.liyun.ui.manager;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.ui.main.activity.ScanActivity;

import butterknife.BindView;

public class DeviceListAct extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
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
        toolbar.setTitle("设备列表");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }

   /* @OnClick(R.id.txt_confirm_submit)
    public void onClick() {
    }*/

}
