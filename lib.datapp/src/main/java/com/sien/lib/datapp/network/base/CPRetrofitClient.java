package com.sien.lib.datapp.network.base;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sien
 * @date 2016/10/20
 * @descript 网络请求对象retrofit client
 */
public class CPRetrofitClient {

    private static int timeout = 20;

    private static JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(json.getAsLong());
        }
    };

    private  final static Gson mgson = new GsonBuilder().registerTypeAdapter(Date.class, deser).setDateFormat(DateFormat.LONG).setLenient().create();

    /**
     * 网络请求对象，开启网络缓存机制、cooike机制，日志拦截，输入字符串参数，输出jsonbean
     * @param context
     * @param baseUrl
     * @return
     */
    public static Retrofit getNetCacheWithCookieRetrofitClient(Context context, String baseUrl){
            //net cache
            Context appContext = context.getApplicationContext();
            //cp.add (测试代码没有ApplicationContext)
            if (appContext == null){
                appContext = context;
            }
            File httpCacheDirectory = new File(appContext.getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024;
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            CPCacheControlInterceptor CPCacheControlInterceptor = new CPCacheControlInterceptor(appContext);

            // log用拦截器
            CPLoggingInterceptor loggingInterceptor = new CPLoggingInterceptor();

            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .cache(cache)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(CPCacheControlInterceptor)
                    .addNetworkInterceptor(CPCacheControlInterceptor)
                    .addInterceptor(new ReceivedCookiesInterceptor(appContext))
                    .addInterceptor(new AddCookiesInterceptor(appContext))
//                    .cookieJar(cookieJar)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Response response = chain.proceed(chain.request());
                            return response;
                        }
                    })
                    .build();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(StringConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(mgson))
                    .client(okClient)
                    .build();

        return client;
    }

    /**
     * 网络请求对象，开启网络缓存机制、cooike机制，日志拦截，输入字符串参数，输出jsonbean
     * @param context
     * @param baseUrl
     * @return
     */
    public static Retrofit getCookieRetrofitClient(Context context, String baseUrl){
        CPLoggingInterceptor loggingInterceptor = new CPLoggingInterceptor();

        //net cache
        Context appContext = context.getApplicationContext();
        //cp.add (测试代码没有ApplicationContext)
        if (appContext == null){
            appContext = context;
        }

        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new ReceivedCookiesInterceptor(appContext))
                .addInterceptor(new AddCookiesInterceptor(appContext))
                .addInterceptor(loggingInterceptor)
//                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response;
                    }
                })
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mgson))
                .client(okClient)
                .build();

        return client;
    }

    public static Retrofit getCookieRetrofitClient(Context context, String baseUrl, int timeOut){
        CPLoggingInterceptor loggingInterceptor = new CPLoggingInterceptor();

        //net cache
        Context appContext = context.getApplicationContext();
        //cp.add (测试代码没有ApplicationContext)
        if (appContext == null){
            appContext = context;
        }

        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .writeTimeout(timeOut, TimeUnit.SECONDS)
                .addInterceptor(new ReceivedCookiesInterceptor(appContext))
                .addInterceptor(new AddCookiesInterceptor(appContext))
                .addInterceptor(loggingInterceptor)
//                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response;
                    }
                })
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mgson))
                .client(okClient)
                .build();

        return client;
    }

    /**
     * 基本网络请求对象
     * @param baseUrl
     * @return
     */
    public static Retrofit getRetrofitClient(String baseUrl){
        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response;
                    }
                })
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mgson))
                .build();
        return client;
    }

    /**
     * 基本网络请求对象
     * @param baseUrl
     * @return
     */
    public static Retrofit getFileRetrofitClient(String baseUrl){
        OkHttpClient okClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());
                        return response;
                    }
                })
                .build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okClient)
                .addConverterFactory(OutStringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mgson))
                .build();
        return client;
    }
}
