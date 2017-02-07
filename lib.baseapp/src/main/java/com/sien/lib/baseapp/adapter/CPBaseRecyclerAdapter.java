package com.sien.lib.baseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sien
 * @date 2016/8/29
 * @descript 基类RecyclerAdater
 */
public abstract class CPBaseRecyclerAdapter<T> extends RecyclerView.Adapter<CPRecyclerHolder> {
    protected int mItemLayoutId;
    protected boolean isScrolling;
    protected Context cxt;
    protected OnItemClickListener listener;
    protected OnItemLongClickListener longClickListener;
    protected ArrayList<T> realDatas;

    protected int itemHeight;

    public int getItemHeight(){ return itemHeight;}

    public CPBaseRecyclerAdapter(final RecyclerView v, Collection<T> datas, int itemLayoutId) {
        if(datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (ArrayList<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        mItemLayoutId = itemLayoutId;
        cxt = v.getContext();

//        v.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
//                if (!isScrolling) {
//                    notifyDataSetChanged();
//                }
//            }
//        });


//        v.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                itemHeight= v.getMeasuredHeight();
//                return true;
//            }
//        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Object data, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,Object data,int position);
    }

    /**
     * Recycler适配器填充方法p
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert(CPRecyclerHolder holder, T item, int position, boolean isScrolling);


    @Override
    public CPRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cxt);
        View root = inflater.inflate(mItemLayoutId, parent, false);
        return new CPRecyclerHolder(root);
    }


    @Override
    public void onBindViewHolder(CPRecyclerHolder holder, int position) {
        convert(holder, realDatas.get(position), position, isScrolling);
        holder.itemView.setOnClickListener(getOnClickListener(position));
        holder.itemView.setOnLongClickListener(getOnLongClickListener(position));
    }


    @Override
    public int getItemCount() {
        return realDatas.size();
    }


    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public void removeItemClickListener(){
        listener = null;
    }

    public void remoeItemLongClickListener() {
        longClickListener = null;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void releaseMemory(){
        if (realDatas != null){
            realDatas.clear();
        }
        realDatas = null;

        if (listener != null){
            listener = null;
        }

        cxt = null;
    }


    public View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && v != null) {
                    listener.onItemClick(v, realDatas.get(position), position);
                }
            }
        };
    }

    public View.OnLongClickListener getOnLongClickListener(final int position){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null && v != null) {
                    longClickListener.onItemLongClick(v, realDatas.get(position), position);
                }
                return false;
            }
        };
    }

    public CPBaseRecyclerAdapter<T> refresh(Collection<T> datas) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (ArrayList<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        return this;
    }

}
