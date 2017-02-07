package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;

import com.sien.aimanager.R;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/7
 * @descript 封面图片adapter
 */
public class CoverAdapter extends CPBaseRecyclerAdapter<String> {
    public CoverAdapter(RecyclerView v, Collection<String> datas){
        super(v,datas, R.layout.item_cover_adapter);
    }

    @Override
    public void convert(CPRecyclerHolder holder, String item, int position, boolean isScrolling) {
        holder.setImageSource(R.id.cover_image,item);
    }
}
