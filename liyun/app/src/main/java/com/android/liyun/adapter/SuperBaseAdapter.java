package com.android.liyun.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;


import com.android.liyun.holder.BaseHolder;

import java.util.List;

public abstract class SuperBaseAdapter<T> extends BaseAdapter { //implements AdapterView.OnItemClickListener {
    private List<T> mDatas;
    private AbsListView mListView;

    public SuperBaseAdapter(AbsListView listView, List<T> datas) {
        this.mDatas = datas;
        this.mListView = listView;
        // mListView.setOnItemClickListener(this);
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }// 添加加载更多的item
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if (convertView == null) {
            holder = getItemHolder(position);
            convertView = holder.getRootView();
        } else {// 有复用
            holder = (BaseHolder) convertView.getTag();
        }
        T data = mDatas.get(position);
        // 给holder中的view设置数据
        holder.setData(data);
        return convertView;
    }

    protected abstract BaseHolder<T> getItemHolder(int position);

    protected void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
