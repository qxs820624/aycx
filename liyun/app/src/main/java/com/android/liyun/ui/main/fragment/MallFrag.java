package com.android.liyun.ui.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.RecyclerAdapter;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.CommendBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.liyun.http.RequestWhatI.TUIJIAN;

public class MallFrag extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    private List<String> picList;
    private List<Map<String, Object>> channelList;
    private List<Integer> girlList;
    private List<String> normalList;
    private RecyclerAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_mail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mApi = new Api(handler, UIUtils.getContext());
        init();
        initData();
        mApi.tuiJian(TUIJIAN);
    }

    private void init() {
        picList = new ArrayList<>();
        channelList = new ArrayList<>();
        girlList = new ArrayList<>();
        normalList = new ArrayList<>();
        //设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(UIUtils.getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txtTitle.setText("商城");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case TUIJIAN:
                        CommendBean commendBean = mGson.fromJson(msg.obj.toString(), CommendBean.class);
                        if (commendBean.getStatus().equals(ConstValues.ZERO)) {
                            adapter = new RecyclerAdapter(UIUtils.getContext(), picList, commendBean.getGoods().getOne(),
                                    commendBean.getGoods().getTwo(), commendBean.getGoods().getThree());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Toast.makeText(UIUtils.getContext(), commendBean.getMsg(), Toast.LENGTH_SHORT).show();

                        }

                        break;
                }
            }
        }
    };

    private void initData() {
        //轮播图需要的图片地址
        String picPath = "http://fdfs.xmcdn.com/group27/M04/D4/24/wKgJW1j11VzTmYOeAAG9Mld0icA505_android_large.jpg";
        String picPath1 = "http://fdfs.xmcdn.com/group27/M0A/D4/81/wKgJR1j13gKTWVXaAALwrIB1AVY346_android_large.jpg";
        String picPath2 = "http://fdfs.xmcdn.com/group26/M05/D8/97/wKgJRlj13vqRHDmVAASRJaudX3I424_android_large.jpg";
        picList.add(picPath);
        picList.add(picPath1);
        picList.add(picPath2);

        for (int i = 0; i < 6; i++) {
            girlList.add(R.mipmap.ic_car_service);
        }
        for (int i = 0; i < 20; i++) {
            normalList.add("正常布局" + i);
        }


    }
}
