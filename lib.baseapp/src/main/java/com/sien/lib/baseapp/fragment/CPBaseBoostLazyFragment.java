package com.sien.lib.baseapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * @author sien
 * @date 2016/8/26
 * @descript 基类懒加载fragment(提供全局加载效果、无网络状态效果 -----不要与CPBaseBoostActivity同时使用)
 */
public abstract class CPBaseBoostLazyFragment extends CPBaseBoostFragment{
    private static final String TAG = CPBaseBoostLazyFragment.class.getSimpleName();
    private boolean isPrepared;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
        if (innerPresenter != null)  innerPresenter.onViewResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (innerPresenter != null)  innerPresenter.onViewStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (innerPresenter != null)  innerPresenter.onViewStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }

        if (innerPresenter != null)  innerPresenter.onViewPause();
    }

    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {

    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {

    }
}
