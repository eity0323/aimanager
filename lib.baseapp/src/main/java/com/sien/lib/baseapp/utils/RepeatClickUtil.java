package com.sien.lib.baseapp.utils;

/**
 * @author sien
 * @date 2016/12/30
 * @descript 重复点击判断工具
 */
public class RepeatClickUtil {

    private static int defaultIntervalTime = 800;//重复点击间隔时间
    private static long lastClickTime;//上次点击时间

    /**
     * 判断重复点击
     * @return
     */
    public static boolean isRepeatClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < defaultIntervalTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isRepeatClick(int intervalTime) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < intervalTime) {
            return true;
        }

        lastClickTime = time;
        return false;
    }
}
