package com.sien.lib.baseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sien.lib.baseapp.widgets.recyclerview.CPBoostRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sien
 * @date 2016/8/29
 * @descript 基类增强型RecyclerAdater（header，load,refresh,empty,loading）
 */
public abstract class CPBaseBoostRecyclerAdapter<T> extends RecyclerView.Adapter<CPRecyclerHolder> {

    //use for boost
    // 各种视图类型
    public static final int TYPE_HEADER = 0x100;//头部
    public static final int TYPE_LOADMORE_FOOTER = 0x101;//底部加载
    public static final int TYPE_END_FOOTER = 0x103;//底部
    public static final int TYPE_NORMAL = 0x102;//普通

    private View headerView;
    private View loadMoreView;
    private View endFooterView;
    // 默认禁止下拉刷新
    private boolean isNeedLoadMore = false;//是否需要加载更多
    //----------end

    protected int mItemLayoutId;//布局资源id
    protected boolean isScrolling;//是否正在滑动
    protected Context cxt;//上下文
    protected OnItemClickListener listener;//点击事件
    protected OnItemLongClickListener longClickListener;//长按事件
    protected List<T> realDatas;//数据源

    protected int itemHeight;//高度

    public int getItemHeight(){ return itemHeight;}

    public CPBaseBoostRecyclerAdapter(final CPBoostRecyclerView v, Collection<T> datas, int itemLayoutId) {
        if(datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
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

    //获取记录数
    public int getDataCount(){
        if (realDatas == null){
            return 0;
        }
        return realDatas.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Object data, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, Object data, int position);
    }

    /**
     * Recycler适配器填充方法p
     *
     * @param holder      viewholder
     * @param item        javabean
     * @param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert(CPRecyclerHolder holder, T item, int position, boolean isScrolling);

    public void convertFooter(CPRecyclerHolder holder){

    }

    public void convertHeader(CPRecyclerHolder holder){

    }

    @Override
    public CPRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADMORE_FOOTER) {
            return new CPRecyclerHolder(loadMoreView) {};

        } else if (viewType == TYPE_END_FOOTER) {
            return  new CPRecyclerHolder(endFooterView) {};

        } else if (viewType == TYPE_HEADER) {
            return new CPRecyclerHolder(headerView) {};

        } else {
            LayoutInflater inflater = LayoutInflater.from(cxt);
            View root = inflater.inflate(mItemLayoutId, parent, false);
            return new CPRecyclerHolder(root);
        }
    }

    @Override
    public void onBindViewHolder(CPRecyclerHolder holder,final int position) {
        if (getItemViewType(position) == TYPE_END_FOOTER){
            convertFooter(holder);
        }else if (getItemViewType(position) == TYPE_END_FOOTER){
            convertFooter(holder);
        }else if (getItemViewType(position) == TYPE_HEADER){
            convertHeader(holder);
        }else {
            int reclaimPostion = reclaimPostion(position);
            convert(holder, realDatas.get(reclaimPostion), position, isScrolling);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && v != null) {
                        int reclaimPostion = reclaimPostion(position);
                        listener.onItemClick(v, realDatas.get(reclaimPostion), position);
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null && v != null){
                        int reclaimPostion = reclaimPostion(position);
                        return longClickListener.onItemLongClick(v,realDatas.get(reclaimPostion),position);
                    }
                    return false;
                }
            });
        }
    }

    //cp.add 修复有header时索引溢出问题
    protected int reclaimPostion(int position){
        int reclaimPostion = position;
        if (hasHeaderView()){
            reclaimPostion = position - 1;
        }
        return reclaimPostion;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l){
        longClickListener = l;
    }

    public void removeItemClickListener(){
        listener = null;
        longClickListener = null;
    }

    public void releaseMemory(){
        if (realDatas != null){
            realDatas.clear();
        }
        realDatas = null;

        listener = null;
        longClickListener = null;

        headerView = null;
        endFooterView = null;
        loadMoreView = null;

        cxt = null;
    }

    public CPBaseBoostRecyclerAdapter<T> refresh(Collection<T> datas) {
        if (datas == null) {
            realDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            realDatas = (List<T>) datas;
        } else {
            realDatas = new ArrayList<>(datas);
        }
        return this;
    }

    //use for boost
    /**
     * set footer voew
     *
     * @param loadMoreView
     * @param endFooterView
     */
    public void setFooterView(View loadMoreView, View endFooterView) {
        this.loadMoreView = loadMoreView;
        this.endFooterView = endFooterView;
    }

    /**
     * set header view
     *
     * @param headerView header view
     */
    public void setHeaderView(View headerView) {
        this.headerView = headerView;
    }

    public void setIsNeedLoadMore(boolean isLoadMore) {
        this.isNeedLoadMore = isLoadMore;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == realDatas.size() && hasLoadMoreFooterView() && isNeedLoadMore) {
            return TYPE_LOADMORE_FOOTER;
        } else if (position == realDatas.size() && hasEndFooterView() && !isNeedLoadMore) {
            return TYPE_END_FOOTER;
        } else if (position == 0 && hasHeaderView()) {
            return TYPE_HEADER;
        } else
            return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (realDatas == null){
            return (hasHeaderView() ? 1 : 0) + (isNeedLoadMore ? hasLoadMoreFooterView() ? 1 : 0 : hasEndFooterView() ? 1 : 0);
        }
        return realDatas.size() + (hasHeaderView() ? 1 : 0) + (isNeedLoadMore ? hasLoadMoreFooterView() ? 1 : 0 : hasEndFooterView() ? 1 : 0);
    }

    public boolean hasHeaderView() {
        return headerView != null;
    }

    public boolean hasEndFooterView() {
        return endFooterView != null;
    }

    public boolean hasLoadMoreFooterView() {
        return loadMoreView != null;
    }
    //--------------------end

}
