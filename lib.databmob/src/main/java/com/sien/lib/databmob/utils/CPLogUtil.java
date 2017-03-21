package com.sien.lib.databmob.utils;

import android.util.Log;

/**
 * @author sien
 * @date 2017/1/9
 * @descript 日志统一管理
 */
public class CPLogUtil {
    private static final String TAG = "CPLogUtil";
    public static final boolean IS_DEV = true;

    public static void logDebug(String tag, String msg){
        if (IS_DEV) {
            Log.d(tag, msg);
        }
    }
    public static void logDebug(String msg){
        logDebug(TAG, msg);
    }


    public static void logError(String tag, String msg, Exception e){
        if (IS_DEV) {
            Log.e(tag, msg,e);
        }
    }
    public static void logError(String tag, String msg){
        if (IS_DEV) {
            Log.e(tag, msg);
        }
    }
    public static void logError(String msg){
        logError(TAG,msg);
    }

    public static void logWarn(String tag, String msg){
        if (IS_DEV) {
            Log.w(tag, msg);
        }
    }
    public static void logWarn(String msg){
        logWarn(TAG,msg);
    }


    public static void logInfo(String tag, String msg){
        if (IS_DEV) {
            Log.i(tag,msg);
        }
    }
    public static void logInfo(String msg){
        logInfo(TAG, msg);
    }
}
