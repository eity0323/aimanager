package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.sien.aimanager.model.ILoginViewModel;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.network.result.LoginResult;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/9/30
 * @descript 登录逻辑处理类
 */
public class LoginPresenter extends BusBasePresenter {
    private final int MSG_UPDATE_APPOPERATE = 1;//登录页切换
    private final int MSG_UPDATE_LOGIN = 2;//登录
    private final int SDK_WELOGIN_FLAG = 3;//微信登录回调状态
    private final int MSG_UPDATE_WECHATLOGIN = 4;//校验微信登录

    private ILoginViewModel impl;

    public LoginPresenter(Context context){
        updateMessageHander = new InnerHandler(this);

        mcontext = context;


        impl = (ILoginViewModel) context;
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void APPOperateEventReceiver(BaseAppEvents.APPOperateEvent event){
        if (event != null){
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_APPOPERATE;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Subscribe
    public void LoginEventReceiver(PersonalEvents.LoginEvent event) {
        if (event != null) {
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_LOGIN;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    //接收宿主应用传替的事件(处理微信登录事件)
    @Subscribe
    public void HostAppEventReceiver(Bundle bundle){
        if (bundle != null){
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = SDK_WELOGIN_FLAG;
                msg.obj = bundle;
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Subscribe
    public void WeChatLoginEventReceiver(PersonalEvents.WeChatLoginEvent event){
        if (event != null) {
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_WECHATLOGIN;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        LoginPresenter theActivity = (LoginPresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }

        if (msg.what == MSG_UPDATE_APPOPERATE){//fragment切换
            if (msg.obj != null){
                APPOperateEventBundle bundle = (APPOperateEventBundle)msg.obj;
                if (bundle != null){
                    if ("fragmentChange".equals(bundle.getString(APPOperateEventBundle.EVENTACTION))){
                        int findex = bundle.getInt("fragmentIndex");
                        impl.refreshFragmentChange(RequestFreshStatus.REFRESH_SUCCESS,findex);
                        return;
                    }
                }
            }
            impl.refreshFragmentChange(RequestFreshStatus.REFRESH_ERROR,-1);
        }else if (msg.what == MSG_UPDATE_LOGIN){//应用登录
//            if (msg.obj != null){
//                LoginResult result = (LoginResult) msg.obj;
//                if (result != null && result.checkRequestSuccess()) {
//                    if (result.getData() != null && result.getData().size() > 0) {
//                        loginResponse = result.getData().get(0);
//                    }
//                    impl.refreshLogin(RequestFreshStatus.REFRESH_SUCCESS);
//                    return;
//                }
//            }
//
//            impl.refreshLogin(RequestFreshStatus.REFRESH_ERROR);
        }else if (msg.what == SDK_WELOGIN_FLAG){//微信登录状态
            if (msg.obj != null){
                Bundle bundle = (Bundle) msg.obj;
                if (bundle != null && "wechatLoginCallBack".equals(bundle.getString("action"))){
                    String status = bundle.getString("loginStatus");
                    String code = bundle.getString("loginCode");
                    if ("success".equals(status)){
                        impl.refreshWechatLoginStatus(RequestFreshStatus.REFRESH_SUCCESS,code);
                        return;
                    }
                }
            }
            impl.refreshWechatLoginStatus(RequestFreshStatus.REFRESH_ERROR,"");
        }else if (msg.what == MSG_UPDATE_WECHATLOGIN){//校验微信登录
            if (msg.obj != null){
                LoginResult result = (LoginResult)msg.obj;
                if (result != null){
//                    if (result.checkRequestSuccess()){
//                        BaseApplication.setSharePerfence(DatappConfig.LOGIN_STATUS_KEY, "true");
//
//                        List<LoginResponse> response = result.getData();
//                        if (response != null && response.size() > 0) {
//
//                            loginResponse = response.get(0);
//
//                            //保存用户类型
//                            BaseApplication.setSharePerfence(CPConfiguration.LOGIN_TYPE_KEY,loginResponse.getUserType());
//                            BaseApplication.setSharePerfence(CPConfiguration.LOGIN_USER_KEY,loginResponse.getUserName());
//                            BaseApplication.setSharePerfence(CPConfiguration.LOGIN_USERID_KEY,loginResponse.getAccountId());
//                            if (!TextUtils.isEmpty(loginResponse.getUserType())) {
//                                CPConfiguration.userType = Integer.valueOf(loginResponse.getUserType());
//                            }
//                            //保存用户帐号
//                            CPConfiguration.userAccount = loginResponse.getUserName();
//                            CPConfiguration.userAccountId = loginResponse.getAccountId();
//                        }
//                        //登录
//                        impl.refreshCheckWechatLogin(RequestFreshStatus.REFRESH_SUCCESS);
//                        return;
//                    }
                }
            }
            impl.refreshCheckWechatLogin(RequestFreshStatus.REFRESH_ERROR);
        }
    }

    @Override
    public void releaseMemory(){
        super.releaseMemory();
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }
}
