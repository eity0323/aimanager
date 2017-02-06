package com.sien.lib.baseapp.beans;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 应用操作参数bundle
 */
public class APPOperateEventBundle {
    public static final String EVENTACTION = "eventAction";

    private Bundle mBundle;

    public APPOperateEventBundle(){
        mBundle = new Bundle();
    }

    public void putString(String key,String value){
        mBundle.putString(key,value);
    }

    public void putInt(String key,int value){
        mBundle.putInt(key,value);
    }

    public void putFloat(String key,float value){
        mBundle.putFloat(key,value);
    }

    public void putSerializable(String key, Serializable value){
        mBundle.putSerializable(key,value);
    }

    public void putParcelable(String key, Parcelable value){
        mBundle.putParcelable(key,value);
    }

    public Object get(String key){
        return mBundle.get(key);
    }

    public String getString(String key){
        return mBundle.getString(key);
    }

    public String getString(String key,String defaultValue){
        return mBundle.getString(key,defaultValue);
    }

    public float getFloat(String key){
        return mBundle.getFloat(key);
    }

    public float getFloat(String key,float defaultValue){
        return mBundle.getFloat(key,defaultValue);
    }

    public int getInt(String key){
        return mBundle.getInt(key);
    }

    public int getInt(String key,int defaultValue){
        return mBundle.getInt(key,defaultValue);
    }

    public Serializable getSerializable(String key){
        return mBundle.getSerializable(key);
    }

    public Parcelable getParcelable(String key){
        return mBundle.getParcelable(key);
    }
}
