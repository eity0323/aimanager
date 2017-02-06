package com.sien.lib.datapp.network.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.sien.lib.datapp.utils.CPLogUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author sien
 * @date 2016/10/14
 * @descript 接收请求中返回并保存cookie
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            List<String> cooklist = originalResponse.headers("Set-Cookie");
            String cookieBuffer  = cookieHeader(cooklist);

            CPLogUtil.logInfo("ReceivedCookies","ReceivedCookies------------>cookie " + cookieBuffer);

            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cookie", cookieBuffer);
                editor.commit();
            }
        }

        return originalResponse;
    }

    //获取cookie
    private String cookieHeader(List<String> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            cookieHeader.append(cookies.get(i));
        }
        return cookieHeader.toString();
    }
}