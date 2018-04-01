package com.android.liyun.adapter;

import android.widget.AbsListView;

import com.android.liyun.bean.CartListBean;
import com.android.liyun.holder.BaseHolder;
import com.android.liyun.holder.CartListHolder;

import java.util.List;

public class CartListAdapter extends SuperBaseAdapter<CartListBean.GoodsBean> {
    List<CartListBean.GoodsBean> datas;

    public CartListAdapter(AbsListView listView, List<CartListBean.GoodsBean> datas) {
        super(listView, datas);
        this.datas = datas;
    }

    @Override
    protected BaseHolder<CartListBean.GoodsBean> getItemHolder(int position) {
        return new CartListHolder();
    }


    public List<CartListBean.GoodsBean> getDatas() {
        return datas;
    }

    public void setDatas(List<CartListBean.GoodsBean> datas) {
        this.datas = datas;
    }
}
