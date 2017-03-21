package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sien.aimanager.R;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;
import com.sien.lib.databmob.beans.PersonSetBean;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 设置adapter
 */
public class SettingAdapter extends CPBaseRecyclerAdapter<PersonSetBean>{
    public SettingAdapter(RecyclerView v, Collection<PersonSetBean> datas){
        super(v,datas,R.layout.item_setting_adapter);
    }

    @Override
    public void convert(CPRecyclerHolder holder, PersonSetBean item, int position, boolean isScrolling) {
        holder.setText(R.id.item_title,item.getDescript());
        holder.setImageSource(R.id.item_icon,item.getResourceId());
        holder.setVisibility(R.id.item_section, realDatas.get(position).getResourceId() == R.mipmap.ic_settings_share? View.VISIBLE:View.GONE);
    }
}
