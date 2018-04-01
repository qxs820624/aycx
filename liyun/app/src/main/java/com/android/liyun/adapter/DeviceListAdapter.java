package com.android.liyun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.Device;

import java.util.List;

/**
 * Created by sunwubin on 2017/11/1.
 */

public class DeviceListAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Device> mList;
    private int mItemLayoutId;
    private CallBack mCallBack;

    public DeviceListAdapter(Context context, List<Device> list, int itemLayoutId, CallBack callBack) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        mItemLayoutId = itemLayoutId;
        mCallBack = callBack;
    }

    public List<Device> getmList() {
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
            holder.txtDel = (TextView) convertView.findViewById(R.id.txt_del);
            holder.txtDeviceName = (TextView) convertView.findViewById(R.id.txt_deviceName);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_default_address);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        Device Device = mList.get(position);
        String deviceName = Device.getName();
        holder.txtDeviceName.setText(deviceName);
        holder.tvAddress.setText(Device.getAddress());
        holder.txtDel.setOnClickListener(this);
        holder.txtDel.setTag(position);
        holder.checkBox.setTag(position);
       // holder.checkBox.setChecked(isDefault.equals(ConstValues.ONE));
        //默认地址
      //  if (isDefault.equals("1")) {
            holder.checkBox.setChecked(true);
       // } else {
            holder.checkBox.setOnClickListener(this);
         //   holder.checkBox.setChecked(false);
        //}
        return convertView;
    }

    @Override
    public void onClick(View v) {
        mCallBack.click(v);
    }

    static class ViewHolder {
        TextView txtDel;
        TextView txtDeviceName;
        TextView tvAddress;
        CheckBox checkBox;
    }
}
