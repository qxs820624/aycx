package com.android.liyun.ui.goods;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.CartListAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.CartListBean;
import com.android.liyun.bean.GoodsDetailBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车
 */
public class CartListAct extends BaseActivity {

    @BindView(R.id.listview)
    ListView listview;
    private String uid;
    private String token;

    @Override
    public int getLayoutId() {
        return R.layout.act_cart_list;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initView() {
        mApi = new Api(handler, UIUtils.getContext());
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("商品详情");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApi.cartList(RequestWhatI.CARTLIST, uid, token);
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case RequestWhatI.CARTLIST://购物车
                        CartListBean cartListBean = mGson.fromJson(msg.obj.toString(), CartListBean.class);
                        if (cartListBean.getStatus().equals(ConstValues.ZERO)) {
                            CartListAdapter cartListAdapter = new CartListAdapter(listview, cartListBean.getGoods());
                            listview.setAdapter(cartListAdapter);

                        } else {
                            Toast.makeText(UIUtils.getContext(), cartListBean.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                        break;
                }
            }
        }
    };
}
