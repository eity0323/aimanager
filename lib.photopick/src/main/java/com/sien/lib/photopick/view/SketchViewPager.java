package com.sien.lib.photopick.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public  class SketchViewPager extends ViewPager {
	private HackyViewPagerDispatchListener mHackyViewPagerDispatchListener;
	public SketchViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public SketchViewPager(Context context, AttributeSet attrs)
	{
	    super(context, attrs);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
	    try
	    {
	            return super.onInterceptTouchEvent(ev);
	    }
	    catch (IllegalArgumentException e)
	    {
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {

	    }
	    return false;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(mHackyViewPagerDispatchListener!=null){
					mHackyViewPagerDispatchListener.isDown();
				}
				break;
			case MotionEvent.ACTION_UP:
				if(mHackyViewPagerDispatchListener!=null){
					mHackyViewPagerDispatchListener.isUp();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if(mHackyViewPagerDispatchListener!=null){
					mHackyViewPagerDispatchListener.isCancel();
				}
				break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * ViewPager上面的触摸事件回调监听接口
	 */
	public interface HackyViewPagerDispatchListener{
		void isDown();
		void isUp();
		void isCancel();
	}
	public HackyViewPagerDispatchListener getmHackyViewPagerDispatchListener() {
		return mHackyViewPagerDispatchListener;
	}

	public void setmHackyViewPagerDispatchListener(HackyViewPagerDispatchListener mHackyViewPagerDispatchListener) {
		this.mHackyViewPagerDispatchListener = mHackyViewPagerDispatchListener;
	}

}
