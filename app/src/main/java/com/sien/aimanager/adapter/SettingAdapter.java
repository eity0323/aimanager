package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;

import com.sien.aimanager.R;
import com.sien.aimanager.beans.SettingBean;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 设置adapter
 */
public class SettingAdapter extends CPBaseRecyclerAdapter<SettingBean> {
    public SettingAdapter(RecyclerView v, Collection<SettingBean> data){
        super(v,data, R.layout.item_setting_adapter);
    }

    @Override
    public void convert(CPRecyclerHolder holder, SettingBean item, int position, boolean isScrolling) {
        holder.setText(R.id.settingTitle,item.getTitle());
        holder.setText(R.id.settingValue,item.getValue());
    }
}
