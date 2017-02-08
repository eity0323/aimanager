package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;

import com.sien.aimanager.R;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;
import com.sien.lib.datapp.beans.AimTypeVO;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 首页适配器
 */
public class AimTypeAdapter extends CPBaseRecyclerAdapter<AimTypeVO> {

    public AimTypeAdapter(RecyclerView v, Collection<AimTypeVO> data){
        super(v,data, R.layout.item_main_adapter);
    }

    @Override
    public void convert(CPRecyclerHolder holder, AimTypeVO item, int position, boolean isScrolling) {
        holder.setText(R.id.item_title,item.getTypeName());
        holder.setText(R.id.item_desc,item.getDesc());
        holder.setImageSource(R.id.item_cover,item.getCover());
    }
}
