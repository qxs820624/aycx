package com.android.liyun.ui.account;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.android.liyun.R;

import com.android.liyun.adapter.CollocationListAdapter;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.OrdersGoodsBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.liyun.http.RequestWhatI.GETORDERLIST;
import static com.android.liyun.http.RequestWhatI.GETORDERLIST_MORE;


/**
 * 商品订单
 */
public class OrdersGoodsFrag extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.swipe_target)
    ExpandableListView listView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    private String uid;
    private String token;
    int pageNo = 1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_order_list_;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mApi = new Api(handler, UIUtils.getContext());
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
        mApi.getOrderList(GETORDERLIST, uid, token, "1");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(UIUtils.getContext());

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1 && !ViewCompat.canScrollVertically(view, 1)) {
                        swipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        listView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onLoadMore() {
        mApi.getOrderList(GETORDERLIST_MORE, uid, token, ++pageNo + "");

        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                mApi.getOrderList(GETORDERLIST, uid, token, "1");
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
                    case GETORDERLIST:

                        String s = msg.obj.toString();
                        OrdersGoodsBean goodsListBean = mGson.fromJson(msg.obj.toString(), OrdersGoodsBean.class);
                        if (null != goodsListBean) {
                            CollocationListAdapter collocationListAdapter = new CollocationListAdapter(getActivity(), listView, goodsListBean.getOrders());
                            listView.setAdapter(collocationListAdapter);
                            listView.expandGroup(0);//默认展开第一个
                            if (null != collocationListAdapter && null != listView) {
                                for (int i = 0; i < collocationListAdapter.getGroupCount(); i++) {
                                    listView.expandGroup(i);
                                }
                            }
//                            ordersListAdapter = new OrdersListAdapter();
//                            mRecyclerView.setAdapter(ordersListAdapter);
//                            ordersListAdapter.addDatas(goodsListBean.getOrders());
                        }

                        break;
                    case GETORDERLIST_MORE:
                        goodsListBean = mGson.fromJson(msg.obj.toString(), OrdersGoodsBean.class);
                        if (null != goodsListBean) {
//                            ordersListAdapter.addDatas(goodsListBean.getOrders());
//                            ordersListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }
    };
}
