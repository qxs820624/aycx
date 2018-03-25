package com.android.liyun.holder;

import android.view.View;
import android.widget.ImageView;

import com.android.liyun.R;
import com.android.liyun.bean.CartListBean;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListHolder extends BaseHolder<CartListBean.GoodsBean> {
    @BindView(R.id.iv_image)
    ImageView ivImage;

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
    }
}
