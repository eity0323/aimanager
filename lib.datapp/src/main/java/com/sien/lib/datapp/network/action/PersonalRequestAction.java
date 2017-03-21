package com.sien.lib.datapp.network.action;

import android.content.Context;

/**
 * @author sien
 * @date 2017/3/16
 * @descript 用户模块请求
 */
public class PersonalRequestAction {
    /**
     * 请求微信登录
     * @param context
     */
    public static void requestWeChatLogin(Context context, final String key){
//        MainRequestApi service = RestClient.getClient(context);
//
//        Call<AimTypeResult> call = service.requestAimType();
//        call.enqueue(new CPBaseCallBack<AimTypeResult>() {
//            @Override
//            public void response(Call<AimTypeResult> call, Response<AimTypeResult> response) {
//                if (response.isSuccessful()) {
//                    AimTypeResult result = response.body();
//                    if (result.checkRequestSuccess()){
//                        List<AimTypeVO> list = result.getData();
//                        if (list != null && list.size() > 0) {
//                            CacheDataStorage.getInstance().writeCacheToStorage(key, result, new CacheDataStorageCallBack() {
//                                @Override
//                                public void complete(BaseResult result) {
//                                    CPLogUtil.logInfo("requestAimTypeDatas", "writeCache success");
//                                }
//                            });
//                        }
//                        EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_SUCCESS,result));
//                        return;
//                    }
//                    //找不到数据，后台处理数据失败，抛异常的处理(返回码不为0)
//                    String msg = result.getMessage();
//                    if (TextUtils.isEmpty(msg)){
//                        msg = "request success operation fail";
//                    }
//                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,msg));
//                } else {
//                    EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_OHTERERROR,"request fail"));
//                }
//            }
//
//            @Override
//            public void fail(Call<AimTypeResult> call, Throwable t) {
//                EventPostUtil.post(new DatappEvent.AimTypeEvent(DatappEvent.STATUS_FAIL_NETERROR,"net error"));
//            }
//        });


    }

}

