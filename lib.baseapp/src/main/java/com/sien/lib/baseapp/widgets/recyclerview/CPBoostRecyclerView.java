package com.sien.lib.baseapp.widgets.recyclerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.adapter.CPBaseBoostRecyclerAdapter;
import com.vlonjatg.progressactivity.ProgressActivity;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 扩展recyclerview,实现下拉刷新，上拉加载，空数据，加载中
 */
public class CPBoostRecyclerView extends FrameLayout {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressActivity progressLayout;
    LinearLayoutManager manager;
    private int mLastVisibleItemPosition;
    private Drawable emptyDrawable;
    private String emptyText;
    private OnClickListener emptyClickListener;

    private CPBaseBoostRecyclerAdapter adapter;

    private OnRcvScrollListener mOnLoadMoreListener;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    private boolean showEmptyView = true;//是否显示无数据布局
    private boolean showRetryButton = true;//是否显示重试按钮

    public CPBoostRecyclerView(Context context) {
        this(context, null);
    }

    public CPBoostRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public void setEnableEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }

    public void setEnableRetryButton(boolean showRetryButton) {
        this.showRetryButton = showRetryButton;
    }

    /**
     * refresh and loadmore are disabled by default
     */
    private void initViews() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_boost_recyclerview, this);
        progressLayout = (ProgressActivity) rootView.findViewById(R.id.base_progress_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.base_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.base_swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),R.color.red_pink));
        disableRefresh();
        disableLoadMore();
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * enable refresh
     *
     * @param onRefreshListener refresh callback
     */
    private void enableRefresh(final OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setEnabled(true);
        mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh();
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        };
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    /**
     * disable refresh
     */
    private void disableRefresh() {
        swipeRefreshLayout.setEnabled(false);
        if (manager != null) {
            if (adapter != null && adapter.getItemCount() != 0)
                manager.scrollToPosition(0);
        }
    }

    /**
     * enable load more
     *
     * @param onLoadMoreListener
     */
    private void enableLoadMore(final OnLoadMoreListener onLoadMoreListener) {
        if (mOnLoadMoreListener != null)
            recyclerView.removeOnScrollListener(mOnLoadMoreListener);
        if (adapter != null)
            adapter.setIsNeedLoadMore(true);
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        mOnLoadMoreListener = new OnRcvScrollListener() {

            @Override
            public void onBottom() {
                onLoadMoreListener.onLoadMore();
            }
        };
        recyclerView.addOnScrollListener(mOnLoadMoreListener);
    }

    /**
     * disable loadmore
     */
    private void disableLoadMore() {
        if (adapter != null) {
            adapter.setIsNeedLoadMore(false);
            adapter.setFooterView(null, null);
        }
        if (mOnLoadMoreListener != null)
            recyclerView.removeOnScrollListener(mOnLoadMoreListener);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setLayoutManager(LinearLayoutManager manager) {
        if (manager != null) {
            this.manager = manager;
            recyclerView.setLayoutManager(manager);
        }
    }

    public void setAdapter(CPBaseBoostRecyclerAdapter adapter) {
        if (this.adapter != null)
            this.adapter = null;

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkIfEmpty();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkIfEmpty();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                checkIfEmpty();
            }

        });
        checkIfEmpty();
    }

    /**
     * show empty view if dataset is empty
     */
    private void checkIfEmpty() {
        if (adapter.getDataCount() == 0) {
            //cp.modify 修复无数据时，无法显示header问题
            if (adapter.hasHeaderView()){
                showContent();
            }else {
                if (showEmptyView) {
                    if (showRetryButton) {
                        showExceptionViewWithListener(emptyDrawable, emptyText, "", "retry", emptyClickListener);
                    }else {
                        showExceptionViewWithoutListener(emptyDrawable,emptyText,"");
                    }
                }else {
                    //cp.add 2017-01-12
//                    showContent();
                }
            }
//            showExceptionViewWithListener(emptyDrawable, emptyText, "", "retry", emptyClickListener);
        } else {
            showContent();
        }
    }

    /**
     * show contentview
     */
    public void showContent() {
        progressLayout.showContent();
    }

    /**
     * show Loadingview
     */
    public void showLoading() {
        progressLayout.showLoading();
    }

    /**
     * show exception view with listener
     *
     * @param exceptionDrawable      exception res
     * @param exceptionTitle         exception title
     * @param exceptionText          exception description
     * @param exceptionButtonTitle   exception retry button
     * @param exceptionClickListener exception button listener
     */
    public void showExceptionViewWithListener(Drawable exceptionDrawable,
                                              String exceptionTitle, String exceptionText,
                                              String exceptionButtonTitle, OnClickListener exceptionClickListener) {
        progressLayout.showError(exceptionDrawable, exceptionTitle, exceptionText, exceptionButtonTitle, exceptionClickListener);

    }

    public void showExceptionViewWithoutListener(Drawable exceptionDrawable,
                                                 String exceptionTitle, String exceptionText) {
        progressLayout.showEmpty(exceptionDrawable, exceptionTitle, exceptionText);
    }

    /**
     * enable or disable refresh
     *
     * @param flag
     */
    public void setRefreshEnabled(boolean flag, OnRefreshListener mRefreshListener) {
        if (flag)
            enableRefresh(mRefreshListener);
        else
            disableRefresh();
    }

    /**
     * enable or disable loadmore
     *
     * @param flag
     */
    public void setLoadMoreEnabled(boolean flag, OnLoadMoreListener mLoadMoreListener) {
        if (flag)
            enableLoadMore(mLoadMoreListener);
        else
            disableLoadMore();
    }

    /**
     * optional setting
     *
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecoration != null)
            recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * optional setting
     *
     * @param colors
     */
    public void setRefreshColorScheme(int... colors) {
        swipeRefreshLayout.setColorSchemeColors(colors);
    }

    /**
     * optional setting
     *
     * @param header
     */
    public void setCustomHeader(View header) {
        if (adapter != null)
            adapter.setHeaderView(header);
    }

    public void setCustomFooter(View loadMoreFooter, View endFooter) {
        if (adapter != null)
            adapter.setFooterView(loadMoreFooter, endFooter);
    }

    public void setEmptyView(Drawable emptyDrawable, String emptyText, OnClickListener emptyClickListener) {
        this.emptyDrawable = emptyDrawable;
        this.emptyText = emptyText;
        this.emptyClickListener = emptyClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void releaseMemory(){
        if (adapter != null){
            adapter.releaseMemory();
            adapter.onDetachedFromRecyclerView(recyclerView);
        }
        adapter = null;

        if (recyclerView != null){
            recyclerView.destroyDrawingCache();
            recyclerView.removeAllViews();
        }
        recyclerView = null;

        if (swipeRefreshLayout != null){
            swipeRefreshLayout.setOnRefreshListener(null);
            swipeRefreshLayout.removeCallbacks(null);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.removeAllViews();
        }

        swipeRefreshLayout = null;
        mOnRefreshListener = null;
        mOnLoadMoreListener = null;
        emptyClickListener = null;

        progressLayout = null;
    }

}
