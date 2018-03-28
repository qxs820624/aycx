package com.android.liyun.ui.goods;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.GoodsListAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.base.GoodsListBean;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.GoodsDetailBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.UIUtils;
import com.android.liyun.widget.LoadMoreFooterView;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.liyun.http.RequestWhatI.FAVORITE;
import static com.android.liyun.http.RequestWhatI.GET_GOODS_DETAIL;
import static com.android.liyun.http.RequestWhatI.GOOD_LIST;
import static com.android.liyun.http.RequestWhatI.GOOD_LIST_MORE;

public class GoodsListAct extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    int pageNo = 1;
    private GoodsListBean goodsListBean;
    private GoodsListAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_common_recycler;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApi = new Api(handler, UIUtils.getContext());
        toolbar.setTitle("商品列表");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApi.goodsList(GOOD_LIST, "1", "1");
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
        LinearLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
    }


    @Override
    public void onLoadMore() {
        mApi.goodsList(GOOD_LIST_MORE, "1", ++pageNo + "");
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                mApi.goodsList(GOOD_LIST, "1", "1");
//                loadDataFromNet(1);
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 1000);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case GOOD_LIST:
                        goodsListBean = mGson.fromJson(msg.obj.toString(), GoodsListBean.class);
                        mAdapter = new GoodsListAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.addDatas(goodsListBean.getGoods());
                        mAdapter.setOnItemClickListener(new GoodsListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, ArrayList<GoodsListBean.GoodsBean> mDatas) {
                                GoodsListBean.GoodsBean goodsBean = mDatas.get(position);
                                Intent intent = new Intent();
                                intent.setClass(UIUtils.getContext(), ComDetailsAct.class);
                                intent.putExtra("id", goodsBean.getGoods_id());
                                Toast.makeText(UIUtils.getContext(),goodsBean.getGoods_id(),Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });
                        break;
                    case RequestWhatI.GOOD_LIST_MORE:
                        goodsListBean = mGson.fromJson(msg.obj.toString(), GoodsListBean.class);
                        mAdapter.getmDatas().addAll(goodsListBean.getGoods());
                        mAdapter.notifyDataSetChanged();
                        break;

                }
            }
        }
    };
}
