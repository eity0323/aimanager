package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;

import com.sien.aimanager.R;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;
import com.sien.lib.datapp.beans.AimItemVO;

import java.util.Collection;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 目标记录adapter
 */
public class AimBeanItemAdapter extends CPBaseRecyclerAdapter<AimItemVO> {

    public AimBeanItemAdapter(RecyclerView v, Collection<AimItemVO> data){
        super(v,data, R.layout.item_aimbean_litem_adapter);
    }

    @Override
    public void convert(CPRecyclerHolder holder, final AimItemVO item, final int position, boolean isScrolling) {
        holder.setText(R.id.aimItemType,item.getAimName());
        if (item.getFinishStatus() == AimItemVO.STATUS_DONE){
            holder.setImageSource(R.id.aimItemStatus,R.mipmap.icon_check_green);
        }else {
            holder.setImageSource(R.id.aimItemStatus,R.mipmap.icon_uncheck);
        }
    }
}
