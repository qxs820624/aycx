package com.android.liyun.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.android.liyun.R;
import com.android.liyun.bean.SiteBean;
import com.android.liyun.http.ConstValues;

import java.util.List;

/**
 * Created by sunwubin on 2017/11/1.
 */

public class SitesAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<SiteBean.AddressBean> mList;
    private int mItemLayoutId;
    private CallBack mCallBack;

    public SitesAdapter(Context context, List<SiteBean.AddressBean> list, int itemLayoutId, CallBack callBack) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        mItemLayoutId = itemLayoutId;
        mCallBack = callBack;
    }

    public List<SiteBean.AddressBean> getmList() {
        return mList;
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
            convertView = mInflater.inflate(mItemLayoutId, null);
            holder = new ViewHolder();
            holder.txtDelAdd = (TextView) convertView.findViewById(R.id.txt_del_add);
            holder.txtConsignee = (TextView) convertView.findViewById(R.id.txt_consignee);
            holder.txtTelephone = (TextView) convertView.findViewById(R.id.txt_telephone);
            holder.txtDeliveryAddress = (TextView) convertView.findViewById(R.id.txt_deliveryAddress);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_default_address);
            holder.txtEdit = (TextView) convertView.findViewById(R.id.txt_edit);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        SiteBean.AddressBean receivingAddressListBean = mList.get(position);
        String consignee = receivingAddressListBean.getName();
        String telephone = receivingAddressListBean.getTelephone();
        String deliveryAddress = receivingAddressListBean.getAddress();
        String isDefault = receivingAddressListBean.getIsdefault();
        String province = receivingAddressListBean.getProvince();
        String city = receivingAddressListBean.getCity();
        String county = receivingAddressListBean.getCountry();
        holder.txtConsignee.setText(consignee);
        holder.txtTelephone.setText(telephone);
        holder.txtDeliveryAddress.setText(province + city + county + deliveryAddress);
        holder.txtDelAdd.setOnClickListener(this);
        holder.txtEdit.setOnClickListener(this);
        holder.txtDelAdd.setTag(position);
        holder.txtEdit.setTag(position);
        holder.checkBox.setTag(position);
       // holder.checkBox.setChecked(isDefault.equals(ConstValues.ONE));
        //默认地址
        if (isDefault.equals("1")) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setOnClickListener(this);
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallBack.click(v);
    }

    static class ViewHolder {
        TextView txtDelAdd;
        TextView txtConsignee;
        TextView txtTelephone;
        TextView txtDeliveryAddress;
        TextView txtEdit;
        CheckBox checkBox;
    }
}
