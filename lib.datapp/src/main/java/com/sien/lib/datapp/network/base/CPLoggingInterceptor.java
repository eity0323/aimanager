package com.sien.lib.datapp.network.base;

import com.sien.lib.datapp.utils.CPLogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sien
 * @date 2016/10/19
 * @descript 日志拦截器 (暂未使用)
 */
public class CPLoggingInterceptor implements Interceptor {
    private String TAG = "[CPLoggingInterceptor]";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        CPLogUtil.logInfo(TAG,String.format("Sending request %s on %s%n%s", request.url(),  chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        CPLogUtil.logInfo(TAG,String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }
}
