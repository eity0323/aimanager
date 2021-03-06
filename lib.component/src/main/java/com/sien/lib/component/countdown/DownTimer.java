/*
    Suneee Android Client, DownTimer
    Copyright (c) 2014 Suneee Tech Company Limited
 */
package com.sien.lib.component.countdown;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * [倒计时类]
 * 
 * @author sien
 * @version 1.0
 * @date 2016-5-13
 * 
 **/
public class DownTimer {

	private final String TAG = DownTimer.class.getSimpleName();
	private CountDownTimer mCountDownTimer;
	private DownTimerListener listener;
	

	/**
	 * [开始倒计时功能]<BR>
	 * [倒计为time长的时间，时间间隔为每秒]
	 * @param time
	 */
	public void startDown(long time){
		startDown(time, 1000);
	}
	
	/**
	 * [倒计为time长的时间，时间间隔为mills]
	 * @param time
	 * @param mills
	 */
	public void startDown(long time, long mills){
		mCountDownTimer = new CountDownTimer(time, mills){
			@Override
			public void onTick(long millisUntilFinished) {
				if(listener != null){
					listener.onTick(millisUntilFinished);
				}else{
					Log.e(TAG, "DownTimerListener 监听不能为空");
				}
			}

			@Override
			public void onFinish() {
				if(listener != null){
					listener.onFinish();
				}else{
					Log.e(TAG, "DownTimerListener 监听不能为空");
				}
				if(mCountDownTimer != null)mCountDownTimer.cancel();
			}
			
		}.start();
	}
	
	/**
	 * [停止倒计时功能]<BR>
	 */
	public void stopDown(){
		if(mCountDownTimer != null)mCountDownTimer.cancel();
	}
	
	/**
	 * [设置倒计时监听]<BR>
	 * @param listener
	 */
	public void setListener(DownTimerListener listener) {
		this.listener = listener;
	}
}

