package com.sien.lib.datapp.network.action;

import android.content.Context;
import android.text.TextUtils;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.cache.CacheDataStorage;
import com.sien.lib.datapp.cache.CacheDataStorageCallBack;
import com.sien.lib.datapp.control.CPBaseCallBack;
import com.sien.lib.datapp.control.JsonMananger;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.RestClient;
import com.sien.lib.datapp.network.RestClientEx;
import com.sien.lib.datapp.network.request.FeedbackRequest;
import com.sien.lib.datapp.network.result.AimItemResult;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.network.result.UploadImageResult;
import com.sien.lib.datapp.network.result.VersionCheckResult;
import com.sien.lib.datapp.utils.CPDeviceUtil;
import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.datapp.utils.EventPostUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
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
     * 提交反馈
     */
    public static void requestFeedback(Context context,String content,String uploadpath){
        MainRequestApi service = RestClient.getClient(context);

        FeedbackRequest request = new FeedbackRequest();
        request.setContent(content);
        request.setStatus("0");
        request.setAppPlat("android");
        String sysVersion = CPDeviceUtil.getSystemVersion();
        if (!TextUtils.isEmpty(sysVersion) && !sysVersion.toLowerCase().contains("android")){
            sysVersion = "Android " + sysVersion;
        }
        request.setTerminalOs(sysVersion);
        request.setIcon(uploadpath);
        request.setAppVersion(CPDeviceUtil.getVersionName(context));
        request.setTerminalType(CPDeviceUtil.getBrand());//getBrand or getMobileModel

        try {
            String jsonContent = JsonMananger.beanToJson(request);

            Call<BaseResult> call = service.submitFeedback(jsonContent);
            call.enqueue(new CPBaseCallBack<BaseResult>() {
                @Override
                public void response(Call<BaseResult> call, Response<BaseResult> response) {
                    if (response.isSuccessful()) {
                        BaseResult result = response.body();
                        if (result.checkRequestSuccess()) {
                            EventPostUtil.post(new DatappEvent.FeedbackEvent(DatappEvent.STATUS_SUCCESS, result));
                            return;
                        }
                        //找不到商品，后台处理数据失败，抛异常的处理(返回码不为0)
                        String msg = result.getMessage();
                        if (TextUtils.isEmpty(msg)){
                            msg = "request success operation fail";
                        }
                        EventPostUtil.post(new DatappEvent.FeedbackEvent(DatappEvent.STATUS_FAIL_OHTERERROR, msg));
                    } else {
                        EventPostUtil.post(new DatappEvent.FeedbackEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "request fail"));
                    }
                }

                @Override
                public void fail(Call<BaseResult> call, Throwable t) {
                    EventPostUtil.post(new DatappEvent.FeedbackEvent(DatappEvent.STATUS_FAIL_NETERROR, "net error"));
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();

            EventPostUtil.post(new DatappEvent.FeedbackEvent(DatappEvent.STATUS_FAIL_OHTERERROR,"data transfer error"));
        }
    }

    /**
     * 上传多张图片
     * @param context
     * @param pathList
     */
    public static void requestUploadMultiFile(Context context,List<String> pathList,String modelName){
        MainRequestApi service = RestClientEx.getClient();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("model",modelName);
        if(pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                builder.addFormDataPart("file"+i,file.getName(), MultipartBody.create(MultipartBody.FORM,file));//MediaType.parse("image/png"),file));
            }
        }

        Call<String> call = service.uploadMultipleFile(builder.build().parts());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                if (response.isSuccessful()){
                    String res = response.body();
                    if (!TextUtils.isEmpty(res)) {
                        try {
                            UploadImageResult result = JsonMananger.jsonToBean(res, UploadImageResult.class);
                            if (result.checkRequestSuccess()) {
                                EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_SUCCESS, result));
                                return;
                            }
                            //找不到商品，后台处理数据失败，抛异常的处理(返回码不为0)
                            String msg = result.getMessage();
                            if (TextUtils.isEmpty(msg)){
                                msg = "request success operation fail";
                            }
                            EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_FAIL_OHTERERROR, msg));
                        }catch (Exception ex){
                            ex.printStackTrace();
                            EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "json error"));
                        }
                    }else {
                        EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "result is null"));
                    }
                }else {
                    EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_FAIL_OHTERERROR, "request fail"));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                EventPostUtil.post(new DatappEvent.UploadImageEvent(DatappEvent.STATUS_FAIL_NETERROR, "net error"));
            }
        });
    }

    /**
     * 请求版本校验
     */
    public static void requestVersionCheck(Context context){
        MainRequestApi service = RestClient.getClient(context);

        Call<VersionCheckResult> call = service.requestVersionCheck();
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
