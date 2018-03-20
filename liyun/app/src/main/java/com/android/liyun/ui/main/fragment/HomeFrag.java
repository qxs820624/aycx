package com.android.liyun.ui.main.fragment;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.Water;
import com.android.liyun.widget.WaterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFrag extends BaseFragment {
    @BindView(R.id.wv_water)
    WaterView mWaterView;

    private List<Water> mWaters;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_home;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mWaters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mWaters.add(new Water((int) (i + Math.random() * 4), "item" + i));
        }
        mWaterView.setWaters(mWaters);
    }

}
