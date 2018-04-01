package com.android.liyun.ui.main.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.LiyunApp;
import com.android.liyun.service.BleService;
import com.android.liyun.ui.account.SetAct;
import com.android.liyun.ui.goods.GoodsListAct;
import com.android.liyun.ui.login.LoginAct;
import com.android.liyun.ui.main.fragment.ForumFrag;
import com.android.liyun.ui.main.fragment.GameFrag;
import com.android.liyun.ui.main.fragment.HomeFrag;
import com.android.liyun.ui.main.fragment.MallFrag;
import com.android.liyun.ui.main.fragment.MyFrag;
import com.android.liyun.utils.BottomNavigationViewHelper;
import com.android.liyun.utils.StatusBarUtil;
import com.android.liyun.utils.UIUtils;
import com.android.liyun.widget.NoSlidingViewPaper;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;

import butterknife.BindView;
import io.realm.Realm;


public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    //值唯一即可,这是为了返回做标识使用
    private final int REQUEST_SETTING = 10;
    private static final int EXIT_APP_DELAY = 1000;
    private long lastTime = 0;
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
        mLiYunApp = LiyunApp.instance();
        mSennoSmartBleService = mLiYunApp.getSennoSmartBleService();
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

    }

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

}
