package com.sien.aimanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.adapter.CPRecyclerHolder;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.CoverIcon;
import com.sien.lib.datapp.utils.CPDateUtil;

import java.util.Collection;
import java.util.Date;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 首页适配器
 */
public class AimTypeAdapter extends CPBaseRecyclerAdapter<AimTypeVO> {
    private Date nowDate;
    private Context mcontext;

    public AimTypeAdapter(RecyclerView v, Collection<AimTypeVO> data){
        super(v,data, R.layout.item_main_adapter);
        nowDate = new Date();
        mcontext = v.getContext();
    }

    @Override
    public void convert(CPRecyclerHolder holder, AimTypeVO item, int position, boolean isScrolling) {
        holder.setText(R.id.item_title,item.getTypeName());
        holder.setText(R.id.item_desc,item.getDesc());

        String cover;
        if (!TextUtils.isEmpty(item.getCover())) {
            cover = item.getCover();
        }else {
            cover = CoverIcon.ICON_OTHER.getValue();
        }
        holder.setImageSource(R.id.item_cover, AppConfig.getResIdByString(mcontext,cover));

        if (item.getPriority() != null){
            if (item.getPriority() == 5) {
                holder.setImageSource(R.id.item_tag, R.mipmap.icon_priority5);
            }else if (item.getPriority() == 4) {
                holder.setImageSource(R.id.item_tag, R.mipmap.icon_priority4);
            }else if (item.getPriority() == 3) {
                holder.setImageSource(R.id.item_tag, R.mipmap.icon_priority3);
            }else if (item.getPriority() == 2) {
                holder.setImageSource(R.id.item_tag, R.mipmap.icon_priority2);
            }else if (item.getPriority() == 1) {
                holder.setImageSource(R.id.item_tag, R.mipmap.icon_priority1);
            }
        }

        if (item.checkCountdown()){
            holder.setVisibility(R.id.item_countdown, View.VISIBLE);
            if (item.getEndTime() != null) {
                int diff = CPDateUtil.getTimeDiffDays(item.getEndTime(), nowDate);
                if (diff > 0) {
                    holder.setText(R.id.item_countdown, "剩余" + diff + "天");
                }else {
                    holder.setText(R.id.item_countdown, "已到期");
                }
            }else {
                holder.setVisibility(R.id.item_countdown, View.GONE);
            }
        }else {
            holder.setVisibility(R.id.item_countdown, View.GONE);
        }
    }
}
