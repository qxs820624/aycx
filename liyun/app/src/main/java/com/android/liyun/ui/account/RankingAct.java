package com.android.liyun.ui.account;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.adapter.CommonAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.ui.main.fragment.MyFrag;
import com.android.liyun.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RankingAct extends BaseActivity {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_title)
    TextView txttitle;

    private List<Fragment> list_fragment;                                //定义要装fragment的列表
    private List<String> list_title;

    @Override
    public int getLayoutId() {
        return R.layout.fra_com_tab;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("全部订单");
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
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                UIUtils.setIndicator(tablayout, 60, 60);
            }
        });
        initControls();
    }

    /**
     * 初始化各控件
     */
    private void initControls() {
        list_fragment = new ArrayList<>();
        list_fragment.add(new OrdersGoodsFrag());
        list_fragment.add(new MyFrag());
        list_title = new ArrayList<>();
        list_title.add("积分排名");
        list_title.add("评分排名");
        //设置TabLayout的模式
        tablayout.setTabMode(TabLayout.MODE_FIXED);

        //为TabLayout添加tab名称
        tablayout.addTab(tablayout.newTab().setText(list_title.get(0)));
        tablayout.addTab(tablayout.newTab().setText(list_title.get(1)));
        CommonAdapter adapter = new CommonAdapter(getSupportFragmentManager(), list_fragment, list_title);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.act_ranking);
//    }
}
