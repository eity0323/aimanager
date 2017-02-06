package com.sien.lib.datapp.network.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.sien.lib.datapp.utils.CPLogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author sien
 * @date 2016/10/14
 * @descript 向请求中添加cookie
 */
public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);

            String cookie = sharedPreferences.getString("cookie", "");

            CPLogUtil.logInfo("AddCookies", "AddCookies------------>cookie " + cookie);

            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }


}
