package com.android.liyun.ui.account;

import android.graphics.Color;
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
import com.android.liyun.adapter.FavoritesListAdapter;
import com.android.liyun.adapter.OrdersListAdapter;
import com.android.liyun.base.BaseFragment;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.FavoritesListBean;
import com.android.liyun.bean.OrdersGoodsBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import butterknife.BindView;

import static com.android.liyun.http.RequestWhatI.FAVORITEGOODLIST;
import static com.android.liyun.http.RequestWhatI.GETORDERLIST;
import static com.android.liyun.http.RequestWhatI.GETORDERLIST_MORE;


/**
 * 商品订单
 */
public class OrdersGoodsFrag extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    int pageNo = 1;
    private String uid;
    private String token;
    private OrdersListAdapter ordersListAdapter;
    private OrdersGoodsBean goodsListBean;
    private String uid1;
    private String token1;

    @Override
    protected int getLayoutResource() {
        return R.layout.fra_order_list;
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
                        goodsListBean = mGson.fromJson(msg.obj.toString(), OrdersGoodsBean.class);
                        if (null != goodsListBean) {
                            ordersListAdapter = new OrdersListAdapter();
                            mRecyclerView.setAdapter(ordersListAdapter);
                            ordersListAdapter.addDatas(goodsListBean.getOrders());
                        }

                        break;
                    case GETORDERLIST_MORE:
                        goodsListBean = mGson.fromJson(msg.obj.toString(), OrdersGoodsBean.class);
                        if (null != goodsListBean) {
                            ordersListAdapter.addDatas(goodsListBean.getOrders());
                            ordersListAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        }
    };

}
