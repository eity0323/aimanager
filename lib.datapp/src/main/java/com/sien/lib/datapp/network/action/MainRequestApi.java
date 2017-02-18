package com.sien.lib.datapp.network.action;

import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.network.result.AimItemResult;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.network.result.VersionCheckResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author sien
 * @date 2016/9/28
 * @descript 时尚主编
 */
public interface MainRequestApi {
    @POST(RequestApiConfig.ACTION_VERSIONCHECK)
    Call<AimTypeResult> requestAimType();

    @POST(RequestApiConfig.ACTION_VERSIONCHECK)
    Call<AimItemResult> requestAimItems();

    @POST(RequestApiConfig.ACTION_VERSIONCHECK)
    Call<AimItemResult> requestAimTypeByType(@Path("aimTypeId") String aimTypeId);

    @POST(RequestApiConfig.ACTION_VERSIONCHECK)
    Call<VersionCheckResult> requestVersionCheck(@Path("appType") String deviceType);

    @Multipart
    @POST(RequestApiConfig.ACTION_UPLOADFILE)
    Call<String> uploadMultipleFile(@Part List<MultipartBody.Part> requestBody);

    @POST(RequestApiConfig.ACTION_FEEDBACK)
    Call<BaseResult> submitFeedback(@Body String requestBody);

    @GET
    Call<ResponseBody> getDownloadApk(@Url String url);
}
