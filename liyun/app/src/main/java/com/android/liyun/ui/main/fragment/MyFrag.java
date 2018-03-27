package com.android.liyun.ui.main.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.ui.account.CertificationAct;
import com.android.liyun.ui.goods.ManRecAddAct;
import com.android.liyun.ui.manager.DeviceListAct;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyFrag extends BaseFragment {
    @BindView(R.id.llyt_all_orders)
    RelativeLayout llytAllOrders;
    @BindView(R.id.rlyt_dri_score)
    RelativeLayout rlytDriScore;
    @BindView(R.id.rlyt_my_rank)
    RelativeLayout rlytMyRank;
    @BindView(R.id.rlyt_his_rec)
    RelativeLayout rlytHisRec;
    @BindView(R.id.rlyt_anth)
    RelativeLayout rlytAnth;
    @BindView(R.id.rlyt_my_favorite)
    RelativeLayout rlytMyFavorite;
    Unbinder unbinder;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_my;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

    }


    @OnClick({R.id.llyt_all_orders, R.id.rlyt_dri_score, R.id.rlyt_my_rank, R.id.rlyt_his_rec, R.id.rlyt_anth, R.id.rlyt_my_favorite,R.id.rlyt_my_device_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_all_orders:
                startActivity(ManRecAddAct.class);
                break;
            case R.id.rlyt_dri_score:
                break;
            case R.id.rlyt_my_rank:
                break;
            case R.id.rlyt_his_rec:
                break;
            case R.id.rlyt_anth:
                startActivity(CertificationAct.class);
                break;
            case R.id.rlyt_my_favorite:
                break;
            case R.id.rlyt_my_device_list:
                startActivity(DeviceListAct.class);
                break;
        }
    }
}
