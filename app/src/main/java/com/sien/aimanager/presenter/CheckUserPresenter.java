package com.sien.aimanager.presenter;

import android.os.Message;
import android.text.TextUtils;

import com.sien.aimanager.activity.LoginActivity;
import com.sien.lib.databmob.beans.UserResult;
import com.sien.aimanager.control.BmobUtil;
import com.sien.aimanager.model.ICheckUserViewModel;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.CPLogUtil;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 校验用户处理类
 */
public class CheckUserPresenter extends BusBasePresenter {
    private final int MSG_UPDATE_APPOPERATE = 1;//短信自动填充

    private ICheckUserViewModel impl;

    public CheckUserPresenter(LoginActivity context, ICheckUserViewModel impl){
        updateMessageHander = new InnerHandler(this);

        mcontext = context;

        this.impl = impl;
    }

    public void requestLogin(String mobile,String checkCode){
        BmobUtil.signOrLogin(mobile,checkCode);
    }

    public void requestCheckCode(String mobile){
        BmobUtil.requestSMSCode(mobile);
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void LoginEventReceiver(PersonalEvents.LoginEvent event){
        if (event != null){
            if (event.checkStatus()){
                UserResult user = (UserResult)event.getData();

                if (user != null) {
                    BaseApplication.setSharePerfence(DatappConfig.LOGIN_STATUS_KEY, "true");

                    //保存用户类型
                    BaseApplication.setSharePerfence(DatappConfig.LOGIN_USER_KEY, user.getUsername());
                    BaseApplication.setSharePerfence(DatappConfig.LOGIN_USERID_KEY, user.getObjectId());

                    //保存用户帐号
                    DatappConfig.userAccount = user.getUsername();
                    DatappConfig.userAccountId = user.getObjectId();

                    //登录
                    impl.refreshLogin(RequestFreshStatus.REFRESH_SUCCESS);
                }else {
                    impl.refreshLogin(RequestFreshStatus.REFRESH_NODATA);
                }
            }else {
                String msg = event.getData().toString();
                CPLogUtil.logError("登录错误：" + msg);
                impl.refreshLogin(RequestFreshStatus.REFRESH_ERROR);
            }
        }
    }

    @Subscribe
    public void MsgCodeEventReceiver(PersonalEvents.MsgCodeEvent event){
        if (event != null){
            if (event.checkStatus()){
                impl.refreshMsgCode(RequestFreshStatus.REFRESH_SUCCESS);
            }else {
                impl.refreshMsgCode(RequestFreshStatus.REFRESH_ERROR);
            }
        }
    }


    @Subscribe
    public void APPOperateEventReceiver(BaseAppEvents.APPOperateEvent event) {
        if (event != null) {
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_APPOPERATE;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        CheckUserPresenter theActivity = (CheckUserPresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }

        if (msg.what == MSG_UPDATE_APPOPERATE){
            if (msg.obj != null){
                APPOperateEventBundle bundle = (APPOperateEventBundle)msg.obj;
                if (bundle != null) {
                    if ("fillSMS".equals(bundle.getString(APPOperateEventBundle.EVENTACTION))) {
                        String smsContent = bundle.getString("SMSContent");
                        if (!TextUtils.isEmpty(smsContent)) {
                            impl.refreshFillSMS(RequestFreshStatus.REFRESH_SUCCESS, smsContent);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }
}
