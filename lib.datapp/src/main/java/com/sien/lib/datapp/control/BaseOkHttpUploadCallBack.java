package com.sien.lib.datapp.control;

import com.sien.lib.datapp.beans.UploadImageVO;

import java.util.List;

import retrofit2.Call;

/**
 * Name: BaseOkHttpFileCallBack
 * Author: sien
 * Email:
 * Comment: //上传文件类
 * Date: 2016-07-07 16:27
 */
public abstract class BaseOkHttpUploadCallBack {
    public abstract void onProgress(float progress,long total);
    public abstract void onFail(Call call, Exception e);
    public abstract void onSuccess(List<UploadImageVO> list);
}
