package com.android.liyun.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.CommendBean;
import com.android.liyun.inter.OnItemClickListener;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class CarGoodsAdapter extends RecyclerView.Adapter<CarGoodsAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<CommendBean.GoodsBean.OneBean> oneBeans;

    public CarGoodsAdapter(Context context, List<CommendBean.GoodsBean.OneBean> oneBeans) {
        mContext = context;
        this.oneBeans = oneBeans;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_goods_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final NormalTextViewHolder holder, int position) {
        holder.txtName.setText(oneBeans.get(position).getName());
        holder.price.setText(oneBeans.get(position).getPay_points());
        Glide.with(UIUtils.getContext()).load(oneBeans.get(position).getImage()).into(holder.ivGoods
        );

        View itemView = ((android.support.v7.widget.CardView) holder.itemView).getChildAt(0);

        if (mOnItemClickListener != null) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return oneBeans == null ? 0 : oneBeans.size();
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGoods;
        TextView txtName;
        TextView price;

        NormalTextViewHolder(View view) {
            super(view);
            ivGoods = (ImageView) view.findViewById(R.id.iv_goods);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            price = (TextView) view.findViewById(R.id.txt_pay_points);
        }
    }

    private OnItemClickListener mOnItemClickListener;//声明接口

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
