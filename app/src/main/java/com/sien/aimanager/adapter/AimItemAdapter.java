package com.sien.aimanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

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
public class AimItemAdapter extends CPBaseRecyclerAdapter<AimItemVO> {

    private ItemButtonClickListener itemButtonClickListener;

    public AimItemAdapter(RecyclerView v, Collection<AimItemVO> data){
        super(v,data, R.layout.item_aimitem_adapter);
    }

    public void setOnItemButtonClickListener(ItemButtonClickListener itemButtonClickListener) {
        this.itemButtonClickListener = itemButtonClickListener;
    }

    @Override
    public void convert(CPRecyclerHolder holder, final AimItemVO item, final int position, boolean isScrolling) {
        holder.setText(R.id.aimItemType,item.getAimName());
        if (item.getFinishStatus() == AimItemVO.STATUS_DONE){
            holder.setImageSource(R.id.aimItemStatus,R.mipmap.icon_check_green);
        }else {
            holder.setImageSource(R.id.aimItemStatus,R.mipmap.icon_uncheck);
        }

        holder.getView(R.id.aimItemEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemButtonClickListener != null){
                    itemButtonClickListener.onItemClick(v,item,position);
                }
            }
        });
    }

    public interface ItemButtonClickListener{
        public void onItemClick(View v,AimItemVO data,int position);
    }
}
