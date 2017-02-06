package com.sien.lib.datapp.control;

import android.text.TextUtils;

import com.sien.lib.datapp.utils.CPLogUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author sien
 * @date 2016/11/16
 * @descript 网络请求回调基类
 */
public abstract class CPBaseCallBack<T> implements retrofit2.Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        response(call,response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String error = "onFailure";
        if (t != null){
            error = t.getMessage();
        }
        if (!TextUtils.isEmpty(error)) {
            CPLogUtil.logError("CPBaseCallBack", error);
        }

        fail(call,t);
    }

    public abstract void response(Call<T> call, Response<T> response);
    public abstract void fail(Call<T> call, Throwable t);
}
