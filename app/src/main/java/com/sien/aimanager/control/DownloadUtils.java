package com.sien.aimanager.control;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.control.BaseOkHttpFileCallBack;
import com.sien.lib.datapp.network.action.MainRequestApi;
import com.sien.lib.datapp.network.download.ProgressListener;
import com.sien.lib.datapp.network.download.ProgressResponseBody;
import com.sien.lib.datapp.utils.CPLogUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author sien
 * @date 2016/10/17
 * @descript 带进度条的下载工具类(升级下载)
 */
public class DownloadUtils {
    private static DownloadUtils intance;
    private Call<ResponseBody> call;

    public static DownloadUtils getInstance(){
        if (intance == null){
            synchronized (DownloadUtils.class){
                if (intance == null) {
                    intance = new DownloadUtils();
                }
            }
        }
        return intance;
    }

    public void ApkDownload(String url,final Context context, final BaseOkHttpFileCallBack callBack){

       final ProgressListener progressListener = new ProgressListener() {
            //该方法在子线程中运行
            @Override
            public void onProgress(long progress, long total, boolean done) {
                CPLogUtil.logDebug("progress:",String.format("%d%% done\n",(100 * progress) / total));
                if (callBack != null){
                    long percent = 0;
                    if (total != 0){
                        percent = (100 * progress) / total;
                    }
                    callBack.onProgress(percent,total);
                }
            }
        };

        //监听下载进度
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //添加拦截器，自定义ResponseBody，添加下载进度
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());

                return originalResponse.newBuilder().body(
                        new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();

            }
        });

        Retrofit client = new Retrofit.Builder()
                .baseUrl(DatappConfig.APP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        MainRequestApi service = client.create(MainRequestApi.class);

        call = service.getDownloadApk(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //下载完成
                if (callBack != null){
                    try {
                        InputStream is = response.body().byteStream();

                        String filePath = getDestFilePath(context,callBack);

                        File file = new File(filePath);
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();
                        }
                        fos.close();
                        bis.close();
                        is.close();

                        callBack.onSuccess(file);

                    } catch (IOException e) {
                        e.printStackTrace();

                        callBack.onFail(call,e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //下载失败
                if (callBack != null){
                    callBack.onFail(call,new Exception(t.getMessage()));
                }
            }
        });
    }

    public void cancelDownload(){
        call.cancel();
    }

    /*获取文件路径*/
    private String getDestFilePath(Context context,BaseOkHttpFileCallBack callBack){
        return getDestFileDir(context,callBack) + getDestFileName(context,callBack);
    }

    /*文件夹名称*/
    private String getDestFileDir(Context context,BaseOkHttpFileCallBack callBack){
        String dir = callBack.getmDestFileDir();
        if (TextUtils.isEmpty(dir)){
            dir = context.getFilesDir().getAbsolutePath();
        }

        File dirFile = new File(dir);
        // 创建目录
        if (!dirFile.exists()) {
            dirFile.mkdirs();// 目录不存在的情况下，创建目录。
        }
        return dir;
    }

    /*文件名称*/
    private String getDestFileName(Context context,BaseOkHttpFileCallBack callBack){
        String fname = callBack.getmDestFileName();
        if (TextUtils.isEmpty(fname)){
            fname = "shoeslife.apk";
        }

        if (fname != null && !fname.contains(".apk")){
            fname = fname + ".apk";
        }

        return fname;
    }
}
