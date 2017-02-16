package com.sien.aimanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sien.aimanager.R;
import com.sien.aimanager.beans.AimBean;
import com.sien.lib.baseapp.adapter.CPBaseAdapter;
import com.sien.lib.baseapp.adapter.CPViewHolder;
import com.sien.lib.baseapp.utils.CollectionUtils;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/15
 * @descript 目标展示adapter
 */
public class AimBeanAdapter extends CPBaseAdapter<AimBean> {

    public AimBeanAdapter(Context context, Collection<AimBean> data){
        super(context,data, R.layout.item_aimbean_adapter);
    }

    @Override
    public void convert(CPViewHolder holder, AimBean item, int position) {
        holder.setText(R.id.item_aimbean_typename,item.aimTypeVO.getTypeName());

        RecyclerView rv = holder.getView(R.id.item_aimbean_list);
        if (!CollectionUtils.IsNullOrEmpty(item.aimItemVOList)) {
            rv.setVisibility(View.VISIBLE);
            AimBeanItemAdapter beanAdapter = new AimBeanItemAdapter(rv, item.aimItemVOList);
            rv.setAdapter(beanAdapter);
        }else {
            rv.setVisibility(View.INVISIBLE);
        }
    }
}
