package com.android.liyun.ui.goods;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.GoodsDetailBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.liyun.http.RequestWhatI.GET_GOODS_DETAIL;

/**
 * 商品详情
 */
public class ComDetailsAct extends BaseActivity {
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_pay_points)
    TextView txtPayPoints;
    @BindView(R.id.txt_add_cart)
    TextView txtAddCart;
    @BindView(R.id.txt_exchange)
    TextView txtExchange;
    /**
     * 商品的id
     */
    private String goods_id;
    private String uid;
    private String token;

    @Override
    public int getLayoutId() {
        return R.layout.act_com_details;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initView() {
        mApi = new Api(handler, UIUtils.getContext());
        goods_id = getIntent().getStringExtra("goods_id");
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("商品详情");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApi.getGoodsDetail(GET_GOODS_DETAIL, goods_id);
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
                    case GET_GOODS_DETAIL:
                        GoodsDetailBean goodsDetailBean = mGson.fromJson(msg.obj.toString(), GoodsDetailBean.class);
                        if (goodsDetailBean.getStatus().equals(ConstValues.ZERO)) {
                            Glide.with(UIUtils.getContext()).load(goodsDetailBean.getImage()).into(ivImage
                            );
                            txtName.setText(goodsDetailBean.getName());
                            txtPayPoints.setText(goodsDetailBean.getPay_points());

                        } else {
                            Toast.makeText(UIUtils.getContext(), goodsDetailBean.getMsg(), Toast.LENGTH_SHORT).show();

                        }
                        break;

                    case RequestWhatI.ADDCART:
                        BaseBen bean = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (bean.getStatus().equals(ConstValues.ZERO)) {
                            startActivity(CartListAct.class);
                            Toast.makeText(UIUtils.getContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), bean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }
    };

    @OnClick({R.id.txt_add_cart, R.id.txt_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_add_cart:
                mApi.addCart(RequestWhatI.ADDCART, uid, token, goods_id, "1", "fads");
                break;
            case R.id.txt_exchange:
                break;
        }
    }
}
