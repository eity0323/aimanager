package com.sien.lib.baseapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sien
 * @date 2016/11/18
 * @descript 基类baseAdapter
 */
public abstract class CPBaseAdapter<T> extends BaseAdapter {
    protected List<T> realDatas;
    protected int mItemLayoutId;
    protected Context cxt;
    protected OnItemClickListener listener;
    private LayoutInflater mInflater;

    public CPBaseAdapter(Context context, Collection<T> datas, int itemLayoutId) {
        if(datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        mItemLayoutId = itemLayoutId;
        cxt = context;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return realDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return realDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItemLayoutId;
    }

    public void releaseMemory(){
        if (realDatas != null){
            realDatas.clear();
        }
        realDatas = null;

        listener = null;
        mInflater = null;
        cxt = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CPViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemLayoutId, parent, false);
            holder = new CPViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CPViewHolder) convertView.getTag();
        }
        if (position < realDatas.size()){
            convert(holder,realDatas.get(position), position);
        }
        return convertView;
    }

    /**
     * CPBaseAdapter适配器填充方法
     *
     * @param holder      viewholder
     * @param item        javabean
     */
    public abstract void convert(CPViewHolder holder, T item, int position);

    public CPBaseAdapter<T> refresh(Collection<T> datas) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        return this;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Object data, int position);
    }
}
