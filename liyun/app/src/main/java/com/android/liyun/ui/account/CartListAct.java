package com.android.liyun.ui.account;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.adapter.CartAdapter;
import com.android.liyun.adapter.CartListAdapter;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.CartListBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.ui.goods.ModAddAct;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车
 */
public class CartListAct extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.cb_all)
    CheckBox cbAll;
    @BindView(R.id.txt_settlement)
    TextView txtSettlement;
    private String uid;
    private String token;
    private CartListBean cartListBean;
    private CartAdapter cartListAdapter;
    private int position;

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
        cbAll.setOnCheckedChangeListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("我的购物车");
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
                        cartListBean = mGson.fromJson(msg.obj.toString(), CartListBean.class);
                        if (cartListBean.getStatus().equals(ConstValues.ZERO)) {
                            cartListAdapter = new CartAdapter(UIUtils.getContext(), cartListBean.getGoods(), new CartAdapter.CallBack() {
                                @Override
                                public void click(View view) {

                                    switch (view.getId()) {
                                        case R.id.cb_select:
                                            List<CartListBean.GoodsBean> goodsBeans = cartListAdapter.getmList();
                                            position = (int) view.getTag();
                                            
                                            for (int i = 0; i < goodsBeans.size(); i++) {
                                                if (!goodsBeans.get(i).isSelect()) {
                                                    cbAll.setChecked(false);
                                                    break;
                                                }else {

                                                }
                                            }

                                            Toast.makeText(UIUtils.getContext(), position + "", Toast.LENGTH_SHORT).show();
                                            break;
                                    }

                                }
                            });
                            listview.setAdapter(cartListAdapter);
                        } else {
                            Toast.makeText(UIUtils.getContext(), cartListBean.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                        break;
                }
            }
        }
    };

    @OnClick({R.id.cb_all, R.id.txt_settlement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cb_all:

                break;
            case R.id.txt_settlement:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        List<CartListBean.GoodsBean> goodsBeans = cartListAdapter.getmList();
        if (isChecked) {
            for (CartListBean.GoodsBean goodsBean : goodsBeans) {
                goodsBean.setSelect(true);

            }
        } else {
            for (CartListBean.GoodsBean goodsBean : goodsBeans) {
                goodsBean.setSelect(false);
            }
        }

        cartListAdapter.notifyDataSetChanged();

    }
}
