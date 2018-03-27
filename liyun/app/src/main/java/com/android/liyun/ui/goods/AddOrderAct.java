package com.android.liyun.ui.goods;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.liyun.R;
import com.android.liyun.base.BaseActivity;
import com.android.liyun.bean.AddBean;
import com.android.liyun.bean.BaseBen;
import com.android.liyun.bean.GoodsDetailBean;
import com.android.liyun.http.Api;
import com.android.liyun.http.ConstValues;
import com.android.liyun.http.RequestWhatI;
import com.android.liyun.utils.SPUtil;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.addapp.pickers.util.ConvertUtils;

import static com.android.liyun.http.RequestWhatI.ADD_ORDER;
import static com.android.liyun.http.RequestWhatI.GET_DEFAULT_ADD;
import static com.android.liyun.http.RequestWhatI.SENDCODE;

public class AddOrderAct extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_goods)
    ImageView ivGoods;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_des)
    TextView txtDes;
    @BindView(R.id.txt_model)
    TextView txtModel;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_pay_points)
    TextView txtPayPoints;
    @BindView(R.id.llyt_select_address)
    LinearLayout llytSelectAddress;
    @BindView(R.id.txt_add_order)
    TextView txtAddOrder;
    @BindView(R.id.llyt_address)
    LinearLayout llytAddress;
    @BindView(R.id.txt_name_shr)
    TextView txtNameShr;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    /**
     * 商品id
     */
    private String goods_id;
    private String token;
    private String uid;
    private GoodsDetailBean goodsDetailBean;
    private BaseBen baseBen;
    private String addressid;

    @Override
    public int getLayoutId() {
        return R.layout.act_add_order;
    }

    @Override
    public void initPresenter() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initView() {
        mApi = new Api(handler, this);
        token = SPUtil.getString(UIUtils.getContext(), ConstValues.TOKEN, "");
        uid = SPUtil.getString(UIUtils.getContext(), ConstValues.UID, "");
        goodsDetailBean = (GoodsDetailBean) getIntent().getSerializableExtra("goodsDetailBean");
        goods_id = goodsDetailBean.getGoods_id();

        String name = goodsDetailBean.getName();
        String pay_points = goodsDetailBean.getPay_points();
        String image = goodsDetailBean.getImage();
        String price = goodsDetailBean.getPrice();
        String model = goodsDetailBean.getModel();

        Glide.with(UIUtils.getContext()).load(image).into(ivGoods
        );
        txtModel.setText(model);
        txtName.setText(name);
        txtPayPoints.setText(pay_points + "积分");
        txtPrice.setText(price);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("下单");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mApi.getDefaultAdd(RequestWhatI.GET_DEFAULT_ADD, uid, token);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != -1) {
                switch (msg.what) {
                    case SENDCODE:
                        baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case GET_DEFAULT_ADD:
                        AddBean addBean = mGson.fromJson(msg.obj.toString(), AddBean.class);
                        //如果没有地址
                        if (addBean.getAddressid().equals(ConstValues.ZERO)) {
                            llytSelectAddress.setVisibility(View.VISIBLE);
                            llytAddress.setVisibility(View.GONE);

                        } else {
                            llytSelectAddress.setVisibility(View.GONE);
                            llytAddress.setVisibility(View.VISIBLE);
                            String name = addBean.getName();
                            String telephone = addBean.getTelephone();
                            String address = addBean.getAddress();
                            addressid = addBean.getAddressid();//地址id
                            txtNameShr.setText(name);
                            txtPhone.setText(telephone);
                            txtAddress.setText(address);
                        }
                        break;

                    case ADD_ORDER:
                        baseBen = mGson.fromJson(msg.obj.toString(), BaseBen.class);
                        Toast.makeText(UIUtils.getContext(), baseBen.getMsg(), Toast.LENGTH_SHORT).show();
                        if (baseBen.getStatus().equals(ConstValues.ZERO)) {
                            UIUtils.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AddOrderAct.this.finish();
                                }
                            }, 1500);
                        }

                        break;
                }
            }
        }
    };

    @OnClick({R.id.llyt_select_address, R.id.txt_add_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llyt_select_address:
                startActivity(ManRecAddAct.class);
                break;
            case R.id.txt_add_order:
                mApi.addOrder(ADD_ORDER, uid, token, addressid, goods_id, "fadsfads");
                break;
        }
    }
}
