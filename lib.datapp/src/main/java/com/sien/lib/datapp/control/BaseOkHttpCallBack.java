package com.sien.lib.datapp.control;

import okhttp3.Call;

/**
 * Name: PersonalCallBack
 * Author: sien
 * Email:
 * Comment: //为了统一不同okhttp版本参数不一样的问题(使用okhttputils,继承自CallBack，暂未使用)
 * Date: 2016-07-05 16:20
 */
public abstract class BaseOkHttpCallBack{
    public void onError(Call call, Exception e) {
        int id = -1;
        onFail(e);
    }

    public void onResponse(String response) {
        int id = -1;
        onSuccess(response);
    }

    public abstract void onSuccess(String response);
    public abstract void onFail(Exception e);
}
