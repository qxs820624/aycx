package com.android.liyun.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.OrdersBean;
import com.android.liyun.bean.OrdersGoodsBean;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class CollocationListAdapter extends BaseExpandableListAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ExpandableListView elv_collocation;
    private List<OrdersBean> data;

    public CollocationListAdapter(Context context, ExpandableListView elv_collocation, List<OrdersBean> data) {
        this.context = context;
        this.elv_collocation = elv_collocation;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getGood().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getGood().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;//如果子条目需要响应click事件,必需返回true
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentViewHolder parentViewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.collocation_list_item_parent, parent, false);
            parentViewHolder = new ParentViewHolder(convertView);
            convertView.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) convertView.getTag();
        }
        OrdersBean ordersBean = data.get(groupPosition);

        parentViewHolder.txt_order_status.setText(ordersBean.getStatus());
        parentViewHolder.txt_order_num_alias.setText(ordersBean.getOrder_num_alias());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.collocation_list_item_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        OrdersBean.GoodBean goodBean = data.get(groupPosition).getGood().get(childPosition);
        if (!TextUtils.isEmpty(goodBean.getImage())) {
            Glide
                    .with(UIUtils.getContext())
                    .load(goodBean.getImage())
                    .crossFade()
                    .into((childViewHolder.iv_goods));
        }
        childViewHolder.txt_name.setText(goodBean.getName());
//        childViewHolder.tv_goods_title.setText(collocationSkuBean.getSkuTitle());
//        childViewHolder.ll_root_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //进入商品详情页操作
//            }
//        });
//        if (childPosition == data.get(groupPosition).getCollocationSkuDoList().size() - 1) {
//            //当前套餐的最后一个商品
//            childViewHolder.ll_bottom.setVisibility(View.VISIBLE);
//            childViewHolder.tv_collocation_price.setText(data.get(groupPosition).getTotalPrice().toString());
//            childViewHolder.tv_add_cart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //把套餐商品加入购物车操作
//                }
//            });
//        } else {
//            childViewHolder.ll_bottom.setVisibility(View.GONE);
//        }
        return convertView;
    }

    class ParentViewHolder {
        private TextView txt_order_num_alias, txt_order_status;

        private ParentViewHolder(View view) {
            txt_order_num_alias = (TextView) view.findViewById(R.id.txt_order_num_alias);
            txt_order_status = (TextView) view.findViewById(R.id.txt_order_status);
        }
    }

    private class ChildViewHolder {
        private ImageView iv_goods;
        private LinearLayout ll_bottom, ll_root_view;
        private TextView txt_name, txt_total, txt_confirm_payment;

        private ChildViewHolder(View view) {
            iv_goods = (ImageView) view.findViewById(R.id.iv_goods);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_total = (TextView) view.findViewById(R.id.txt_total);
            txt_confirm_payment = (TextView) view.findViewById(R.id.txt_confirm_payment);
        }
    }
}
