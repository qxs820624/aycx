package com.android.liyun.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.CartListBean;
import com.android.liyun.bean.SiteBean;
import com.android.liyun.utils.UIUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sunwubin on 2017/11/1.
 */

public class CartAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CartListBean.GoodsBean> mList;
    private CallBack mCallBack;

    public CartAdapter(Context context, List<CartListBean.GoodsBean> list, CallBack callBack) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        mCallBack = callBack;
    }

    public List<CartListBean.GoodsBean> getmList() {
        return mList;
    }

    public void setmList(List<CartListBean.GoodsBean> mList) {
        this.mList = mList;
    }

    public interface CallBack {
        public void click(View view);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_cart_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        CartListBean.GoodsBean data = mList.get(position);
        Glide.with(UIUtils.getContext()).load(data.getImage()).into(holder.ivImage
        );
        holder.txtModel.setText(data.getModel());
        holder.txtName.setText(data.getName());
        holder.txtPayPoints.setText(data.getPay_points() + "积分");
        holder.cbSelect.setTag(position);
        if (data.isSelect()) {
            holder.cbSelect.setChecked(true);
        } else {
            holder. cbSelect.setOnClickListener(this);
            holder. cbSelect.setChecked(false);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallBack.click(v);
    }

//    static class ViewHolder {
//        TextView txtDelAdd;
//        TextView txtConsignee;
//        TextView txtTelephone;
//        TextView txtDeliveryAddress;
//        TextView txtEdit;
//        CheckBox checkBox;
//    }

    static class ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_model)
        TextView txtModel;
        @BindView(R.id.txt_pay_points)
        TextView txtPayPoints;
        @BindView(R.id.cb_select)
        AppCompatCheckBox cbSelect;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
