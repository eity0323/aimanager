package com.sien.lib.baseapp.widgets;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by user1 on 2016/11/29.
 * 降低了原生下拉刷新的灵敏度
 */
public class CPRefreshView extends SwipeRefreshLayout {
    private float mInitialDownY;
    private int mTouchSlop;

    public CPRefreshView(Context context) {
        this(context, null);
    }

    public CPRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop =(int)(ViewConfiguration.get(context).getScaledTouchSlop()*0.5);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mInitialDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float yDiff = ev.getY() - mInitialDownY;
                if (yDiff < mTouchSlop) {
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }


    /**
     * @return 返回灵敏度数值
     */
    public int getTouchSlop() {
        return mTouchSlop;
    }

    /**
     * 设置下拉灵敏度
     *
     * @param mTouchSlop dip值
     */
    public void setTouchSlop(int mTouchSlop) {
        this.mTouchSlop = mTouchSlop;
    }
}
