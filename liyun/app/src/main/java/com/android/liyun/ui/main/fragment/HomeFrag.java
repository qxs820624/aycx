package com.android.liyun.ui.main.fragment;

import android.util.Log;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.ConnectionTimeBean;
import com.android.liyun.bean.Water;
import com.android.liyun.widget.WaterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HomeFrag extends BaseFragment {
    @BindView(R.id.wv_water)
    WaterView mWaterView;

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
        //初始化数据库
        mRealm = Realm.getDefaultInstance();
        connectionTimeBeans = mRealm.where(ConnectionTimeBean.class).equalTo("isUpload", false).findAllSorted("id", Sort.DESCENDING);
        Log.e("TAG", connectionTimeBeans.size()+"");
        mWaters = new ArrayList<>();
        double energySum = 0;
        for (int i = 0; i <connectionTimeBeans.size(); i++) {
            Log.e("TAG", connectionTimeBeans.get(i).getenergyNum() + "");
            energySum+=connectionTimeBeans.get(i).getenergyNum();
        }
            mWaters.add(new Water(energySum,""));
             mWaterView.setWaters(mWaters);
    }

}
