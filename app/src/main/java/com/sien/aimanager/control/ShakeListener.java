package com.sien.aimanager.control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

/**
 * Name: ShakeListener
 * Author: sien
 * Email:
 * Comment: //监听手机传感器事件
 * Date: 2016-07-25 14:56
 */
public class ShakeListener implements SensorEventListener {
    /**
     * 速度阈值，当摇晃速度达到这值后产生作用
     */
    private static final int SPEED_SHRESHOLD = 3000;
    /**
     * 两次检测的时间间隔
     */
    private static final int UPTATE_INTERVAL_TIME = 70;
    /**
     * 最大间隔时间，超过该时间未再次摇动认为已停止摇动
     */
    private static final int MAX_INTERVAL_TIME = 500;
    /**
     * 摇动监听器
     */
    private OnShakeListener mOnShakeListener;
    /**
     * 传感器管理类
     */
    private SensorManager mSensorManager;
    /**
     * 传感器
     */
    private Sensor mSensor;
    private Handler mHandler;
    // 手机上一个位置时重力感应坐标
    private float mLastX;
    private float mLastY;
    private float mLastZ;
    // 上次检测时间
    private long mLastUpdateTime;

    public ShakeListener(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        // 获得加速度传感器
        if (mSensorManager != null)
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mHandler = new Handler();
    }

    /**
     * 注册监听，本方法应该在Activity的onResume()中调用
     */
    public void register() {
        if (mSensor != null)
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 注销监听器，必须在Activity的onPause()、onStop()、onDestroy()中调用本方法，防止内存溢出同时防止在黑屏或者按下Home键后任可回调摇动
     */
    public void unregister() {
        mSensorManager.unregisterListener(this);
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    /**
     * 设置摇动回调监听
     *
     * @param onShakeListener 摇动回调监听
     */
    public void setOnShakeListener(OnShakeListener onShakeListener) {
        mOnShakeListener = onShakeListener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = currentUpdateTime - mLastUpdateTime;
        // 判断是否达到了检测时间间隔
        if (timeInterval < UPTATE_INTERVAL_TIME)
            return;
        // 现在的时间变成last时间
        mLastUpdateTime = currentUpdateTime;

        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        // 将现在的坐标变成last坐标
        mLastX = x;
        mLastY = y;
        mLastZ = z;
        // 返回最近的双近似的平方根
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
        // 达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {
            mOnShakeListener.onShakeStart();
            if (mHandler != null) {
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, MAX_INTERVAL_TIME);
            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mOnShakeListener.onShakeFinish();
        }
    };

    /**
     * 摇动监听接口
     *
     * @author Jone
     */
    public interface OnShakeListener {
        /**
         * 开始新的一次摇动
         */
        public abstract void onShakeStart();

        /**
         * 停止结束
         */
        public abstract void onShakeFinish();
    }
}
