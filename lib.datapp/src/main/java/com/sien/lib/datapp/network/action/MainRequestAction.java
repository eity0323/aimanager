package com.sien.lib.datapp.network.action;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.cache.CacheDataStorage;
import com.sien.lib.datapp.cache.CacheDataStorageCallBack;
import com.sien.lib.datapp.control.CPBaseCallBack;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.RestClient;
import com.sien.lib.datapp.network.result.AimItemResult;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.network.result.VersionCheckResult;
import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @author sien
 * @date 2016/9/28
 * @descript 网络请求action
 */
public class MainRequestAction {
    /**
     * 请求目标分类数据结构
     * @param context
     */
    public static void requestAimTypeDatas(Context context,final String key){
        MainRequestApi service = RestClient.getClient(context);

        Call<AimTypeResult> call = service.requestAimType();
        call.enqueue(new CPBaseCallBack<AimTypeResult>() {
            @Override
            public void response(Call<AimTypeResult> call, Response<AimTypeResult> response) {
                if (response.isSuccessful()) {
                    AimTypeResult result = response.body();
                    if (result.checkRequestSuccess()){
                        List<AimTypeVO> list = result.getData();
                        if (list != null && list.size() > 0) {
                            CacheDataStorage.getInstance().writeCacheToStorage(key, result, new CacheDataStorageCallBack() {
                                @Override
                                public void complete(BaseResult result) {
                                    CPLogUtil.logInfo("requestAimTypeDatas", "writeCache success");
                                }
                            });
                        }
                        EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS,result));
                        return;
                    }
                    //找不到数据，后台处理数据失败，抛异常的处理(返回码不为0)
                    String msg = result.getMessage();
                    if (TextUtils.isEmpty(msg)){
                        msg = "request success operation fail";
                    }
                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,msg));
                } else {
                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,"request fail"));
                }
            }

            @Override
            public void fail(Call<AimTypeResult> call, Throwable t) {
                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_NETERROR,"net error"));
            }
        });
    }

    /**
     * 请求目标记录项数据结构
     * @param context
     */
    public static void requestAimItemDatas(Context context,Long aimTypeId,final String key){
        MainRequestApi service = RestClient.getClient(context);

        Call<AimItemResult> call = service.requestAimTypeByType(String.valueOf(aimTypeId));
        call.enqueue(new CPBaseCallBack<AimItemResult>() {
            @Override
            public void response(Call<AimItemResult> call, Response<AimItemResult> response) {
                if (response.isSuccessful()) {
                    AimItemResult result = response.body();
                    if (result.checkRequestSuccess()){
                        List<AimItemVO> list = result.getData();
                        if (list != null && list.size() > 0) {
                            CacheDataStorage.getInstance().writeCacheToStorage(key, result, new CacheDataStorageCallBack() {
                                @Override
                                public void complete(BaseResult result) {
                                    CPLogUtil.logInfo("requestAimTypeDatas", "writeCache success");
                                }
                            });
                        }
                        EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS,result));
                        return;
                    }
                    //找不到数据，后台处理数据失败，抛异常的处理(返回码不为0)
                    String msg = result.getMessage();
                    if (TextUtils.isEmpty(msg)){
                        msg = "request success operation fail";
                    }
                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,msg));
                } else {
                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,"request fail"));
                }
            }

            @Override
            public void fail(Call<AimItemResult> call, Throwable t) {
                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_NETERROR,"net error"));
            }
        });
    }

    /**
     * 请求版本校验
     */
    public static void requestVersionCheck(Context context){
        MainRequestApi service = RestClient.getClient(context);

        Call<VersionCheckResult> call = service.requestVersionCheck("android");
        call.enqueue(new CPBaseCallBack<VersionCheckResult>() {
            @Override
            public void response(Call<VersionCheckResult> call, Response<VersionCheckResult> response) {
                if (response.isSuccessful()) {
                    VersionCheckResult result = response.body();
                    if (result.checkRequestSuccess()) {
                        EventPostUtil.post(new DatappEvent.RequestVersionCheckEvent(DatappEvent.STATUS_SUCCESS, result));
                        return;
                    }
                    //找不到商品，后台处理数据失败，抛异常的处理(返回码不为0)
                    String msg = result.getMessage();
                    if (TextUtils.isEmpty(msg)){
                        msg = "request success operation fail";
                    }
                    EventPostUtil.post(new DatappEvent.RequestVersionCheckEvent(DatappEvent.STATUS_FAIL_OHTERERROR, msg));
                } else {
                    EventPostUtil.post(new DatappEvent.RequestVersionCheckEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "request fail"));
                }
            }

            @Override
            public void fail(Call<VersionCheckResult> call, Throwable t) {
                EventPostUtil.post(new DatappEvent.RequestVersionCheckEvent(DatappEvent.STATUS_FAIL_NETERROR, "net error"));
            }
        });
    }

    /**
     * 下载更新包包
     * @param filepath 下载文件保存路径（包含后缀）  （不带进度条下载-暂未使用，DownloadUtils带下载进度的下载）
     */
    public static void requestDownload(Context context,String url,final String filepath){
        MainRequestApi service = RestClient.getClient(context);

        Call<ResponseBody> call = service.getDownloadApk(url);
        call.enqueue(new CPBaseCallBack<ResponseBody>() {
            @Override
            public void response(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    InputStream is = response.body().byteStream();
                    File file = new File(filepath);
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

                    String dirpath = filepath.substring(0,filepath.lastIndexOf("."));
                    EventPostUtil.post(new DatappEvent.DownloadApkEvent(DatappEvent.STATUS_SUCCESS,dirpath));
                } catch (IOException e) {
                    e.printStackTrace();
                    EventPostUtil.post(new DatappEvent.DownloadApkEvent(DatappEvent.STATUS_FAIL_OHTERERROR,"io exception"));
                }
            }


            @Override
            public void fail(Call<ResponseBody> call, Throwable t) {
                EventPostUtil.post(new DatappEvent.DownloadApkEvent(DatappEvent.STATUS_FAIL_NETERROR,"net error"));
            }
        });
    }

    public static void releaseMemory(){
        RestClient.releaseClient();
    }
}
