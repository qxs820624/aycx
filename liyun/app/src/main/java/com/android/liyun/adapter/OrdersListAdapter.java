package com.android.liyun.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.base.GoodsListBean;
import com.android.liyun.bean.OrdersGoodsBean;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunwubin on 2017/10/30.
 */

public class OrdersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private ArrayList<OrdersGoodsBean.OrdersBean> mDatas = new ArrayList<>();

    private View mHeaderView;

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addDatas(List<OrdersGoodsBean.OrdersBean> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) return new ViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orders_list, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) return;

        final int pos = getRealPosition(holder);

        OrdersGoodsBean.OrdersBean ordersBean = mDatas.get(pos);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtOrderNumAlias.setText(ordersBean.getOrder_num_alias());
//            viewHolder.txtCommodityName.setText(goodsBean.getName());
//            viewHolder.txtPrice.setText(goodsBean.getPrice() + "积分兑换");
//            if (mListener == null) return;
//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onItemClick(pos, mDatas);
//                }
//            });


//            int maxHeight = UIUtils.dp2px( 300);
//            Drawable drawable
//            int height = (int) ((float) view.getWidth()/drawable.getMinimumWidth() * drawable.getMinimumHeight());
//            if (height > maxHeight) height = maxHeight;
//            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
//            if (!TextUtils.isEmpty(ordersBean.getGood().get(0).getImage())) {
//                Glide
//                        .with(UIUtils.getContext())
//                        .load(ordersBean.getGood().get(0).getImage())
//                        .crossFade()
//                        .into((viewHolder.ivGoods));
//            }
        }


    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_order_num_alias)
        TextView txtOrderNumAlias;
        @BindView(R.id.iv_goods)
        ImageView ivGoods;
        @BindView(R.id.txt_confirm_payment)
        TextView txtConfirmPayment;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ArrayList<GoodsListBean.GoodsBean> mDatas);
    }
}
