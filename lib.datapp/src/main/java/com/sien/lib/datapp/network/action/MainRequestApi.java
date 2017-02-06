package com.sien.lib.datapp.network.action;

import com.sien.lib.datapp.network.result.AimItemResult;
import com.sien.lib.datapp.network.result.AimTypeResult;
import com.sien.lib.datapp.network.result.VersionCheckResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET
    Call<ResponseBody> getDownloadApk(@Url String url);
}
