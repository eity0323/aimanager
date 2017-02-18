package com.sien.aimanager.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author sien
 * @date 2017/2/18
 * @descript com.sien.aimanager.widgets
 */
public class RecyclerViewTouchable extends RecyclerView {
    public RecyclerViewTouchable(Context context) {
        super(context);
    }

    public RecyclerViewTouchable(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewTouchable(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
