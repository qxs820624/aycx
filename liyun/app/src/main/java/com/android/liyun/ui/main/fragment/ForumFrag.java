package com.android.liyun.ui.main.fragment;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.BaseFragment;

import butterknife.BindView;

public class ForumFrag extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_forum;
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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtTitle.setText("论坛");
        toolbar.setTitleTextColor(Color.WHITE);
    }
}
