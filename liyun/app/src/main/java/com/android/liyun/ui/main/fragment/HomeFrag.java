package com.android.liyun.ui.main.fragment;

import android.content.Context;
import android.util.Log;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.base.ConnectStatusManager;
import com.android.liyun.bean.ConnectionTimeBean;
import com.android.liyun.bean.Water;
import com.android.liyun.ui.main.activity.MainActivity;
import com.android.liyun.utils.ToastUtils;
import com.android.liyun.widget.WaterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HomeFrag extends BaseFragment {
    @BindView(R.id.wv_water)
    WaterView mWaterView;
    private MainActivity mMainActivity;
    private List<Water> mWaters;
    private Realm mRealm;
    private RealmResults<ConnectionTimeBean> connectionTimeBeans;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_home;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mMainActivity = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化数据库
        mRealm = Realm.getDefaultInstance();
        connectionTimeBeans = mRealm.where(ConnectionTimeBean.class).equalTo("isUpload", false).findAllSorted("id", Sort.DESCENDING);
        Log.e("TAG", connectionTimeBeans.size() + "");
        mWaters = new ArrayList<>();
        double energySum = 0;
        for (int i = 0; i < connectionTimeBeans.size(); i++) {
            energySum += connectionTimeBeans.get(i).getEnergyNum();
        }
        Log.e("TAG", "size=" + connectionTimeBeans.size());
        mWaters.add(new Water(energySum, ""));
        mWaterView.setWaters(mWaters);
    }

    //TODO   当点击水滴就上传服务器 上传成功 就更改数据库记录的状态 改为true
    private void updateBean(final ConnectionTimeBean connectionTimeBean) {
        connectionTimeBean.setUpload(true);
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(connectionTimeBean);
            }
        });
    }

    @OnClick(R.id.tv_connect)
    public void onClick() {
        if(!ConnectStatusManager.isConnected()){
            mMainActivity.scanDevice();
        } else {
            ToastUtils.showToast("是以连接状态");
        }
    }
}
