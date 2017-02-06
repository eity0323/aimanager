package com.sien.lib.baseapp.events;

import android.view.View;

import com.sien.lib.baseapp.presenters.BasePresenter;


/**
 * @author sien
 * @date 2016/9/26
 * @descript 页面基本操作
 */
public interface IActivityOperater {
    public void showToast(String msg);

    public void logDebug(String msg);

    public void logError(String msg);

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    public <E extends View> E findView(int viewId);

    public void initViews();

    public void initial();

    /**
     * 创建Presenter, 然后通过调用{@link #getPresenter()}来使用生成的Presenter
     * @return Presenter
     */
    public abstract <V extends BasePresenter> V createPresenter();

    /**
     * 获取通过{@link #createPresenter()}生成的presenter对象
     * @return Presenter
     */
    public <V extends BasePresenter> V getPresenter();

    public void showLoading();

    public void hideLoading();
}
