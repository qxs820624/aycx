package com.android.liyun.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.CartListBean;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListHolder extends BaseHolder<CartListBean.GoodsBean> {
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_model)
    TextView txtModel;
    @BindView(R.id.txt_pay_points)
    TextView txtPayPoints;

    @Override
    protected View initView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_cart_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void refreshUI(CartListBean.GoodsBean data) {
        Glide.with(UIUtils.getContext()).load(data.getImage()).into(ivImage
        );
        txtModel.setText(data.getModel());
        txtName.setText(data.getName());
        txtPayPoints.setText(data.getPay_points() + "积分");
    }
}
