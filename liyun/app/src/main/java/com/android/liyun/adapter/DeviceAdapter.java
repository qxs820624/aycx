package com.android.liyun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.liyun.R;
import com.android.liyun.bean.Device;

import java.util.List;

/**
 * @author hzx
 * @create at 2018/3/18 15:29
 */
public class DeviceAdapter extends BaseAdapter {

    private Context context;
    private List<Device> devices;

    public DeviceAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    public void updateListView(List<Device> devices) {
        this.devices = devices;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return devices==null?0:devices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_device, null);
            mViewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            mViewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            mViewHolder.tvRssi = (TextView) convertView.findViewById(R.id.tv_rssi);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Device device = devices.get(position);
        mViewHolder.tvName.setText(device.getName());
        mViewHolder.tvAddress.setText(device.getAddress());
        mViewHolder.tvRssi.setText(device.getRssi()+"dBm");
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvAddress;
        TextView tvRssi;
    }
}
