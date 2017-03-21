package com.sien.aimanager.activity;

import android.Manifest;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.sien.aimanager.R;
import com.sien.aimanager.fragment.LoginFragment1;
import com.sien.aimanager.fragment.LoginFragment2;
import com.sien.aimanager.fragment.LoginFragment3;
import com.sien.aimanager.model.ILoginViewModel;
import com.sien.aimanager.presenter.LoginPresenter;
import com.sien.aimanager.receiver.SMSBroadcastReceiver;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.utils.CPPatternUtil;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.EventPostUtil;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//import com.sien.lib.databmob.network.action.PersonalRequestAction;

//import com.umeng.analytics.MobclickAgent;


/**
 * @author sien
 * @descript 登录页
 */
public class LoginActivity extends CPBaseActivity implements ILoginViewModel {
    private SMSBroadcastReceiver broadcastReceiver;

    private LoginPresenter presenter;
//    private FragmentManager mFragmentManager;
    private LoginFragment1 loginFragment1;
    private LoginFragment2 loginFragment2;
    private LoginFragment3 loginFragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //申请读取短信内容权限
        readSMSTask();
        setFullScreen();
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    public void initViews(){
        loginFragment1 = new LoginFragment1(this);
        loginFragment2 = new LoginFragment2(this);
        loginFragment3 = new LoginFragment3(this);

        loginFragment1.show(false);
        loginFragment2.hide(false);
        loginFragment3.hide();
    }

    public void initial(){
        presenter = getPresenter();

    }

    @Override
    protected void onDestroy() {
        if (loginFragment1 != null){
            loginFragment1.onDestroy();
        }
        loginFragment1 = null;

        if (loginFragment2 != null){
            loginFragment2.onDestory();
        }
        loginFragment2 = null;

        if (loginFragment3 != null){
            loginFragment3.onDestroy();
        }
        loginFragment3 = null;

        //注销sms接收器
        if (broadcastReceiver != null) {
            broadcastReceiver.removeMessageListener();
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    //注册sms接收器
    private void initBroadcastReceiver(){
        broadcastReceiver = new SMSBroadcastReceiver();

        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(SMSBroadcastReceiver.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        registerReceiver(broadcastReceiver, intentFilter);

        broadcastReceiver.setOnReceivedMessageListener(new SMSBroadcastReceiver.MessageListener() {
            @Override
            public void onReceived(String message) {
                //read sms content
                fillCheckCodeBySMS(message);
            }
        });
    }

    //读取短信内容，填充验证码
    private void fillCheckCodeBySMS(String message){
        if (!TextUtils.isEmpty(message) && message.contains("鞋生活")){
            logDebug(message);

            //发送短信内容
            APPOperateEventBundle bundle = new APPOperateEventBundle();
            bundle.putString(APPOperateEventBundle.EVENTACTION,"fillSMS");
            bundle.putString("SMSContent", CPPatternUtil.filterSMSCode(message));

            EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS,bundle));
        }
    }

    /*登录成功，刷新用户信息*/
    private void postRefreshUserInfoEvent(){
        if (presenter == null)  return;

//        APPOperateEventBundle bundle = new APPOperateEventBundle();
//        bundle.putString(APPOperateEventBundle.EVENTACTION,"loginstatus");
//        bundle.putString("status","success");
//        if (presenter.getLoginResponse() != null){
//            String name = presenter.getLoginResponse().getUserName();
//            if (TextUtils.isEmpty(name)){
//                name = CPStringUtil.serectMobileNumber(presenter.getLoginResponse().getMobile());
//            }
//            bundle.putString("userName",name);
//            bundle.putString("avatar",presenter.getLoginResponse().getPhoto());
//            bundle.putString("userType",presenter.getLoginResponse().getUserType());
//        }
//
//        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS,bundle));
    }

    /*登录成功*/
    private void loginSuccess(){
        postRefreshUserInfoEvent();

        finish();
    }

    @Override
    public void refreshFragmentChange(RequestFreshStatus status, int fragmentIndex) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            if (fragmentIndex == -1){
                finish();
                return;
            }else if(fragmentIndex == 0){
                loginFragment1.show(true);
                loginFragment2.hide(true);
                loginFragment3.hide();
            }else if(fragmentIndex == 1){
                loginFragment1.hide();
                loginFragment2.show(true);
                loginFragment3.hide();
            }else if(fragmentIndex == 2){
                loginFragment1.hide();
                loginFragment2.hide(false);
                loginFragment3.show();
            }
        }
    }

    @Override
    public void refreshWechatLoginStatus(RequestFreshStatus status, String code) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            //调用系统微信校验接口
//            PersonalRequestAction.requestWeChatLogin(this,code);

            //统计登录类型为微信登录
//            MobclickAgent.onProfileSignIn("wechat", CPConfiguration.userAccountId);
        }else {
            showToast("微信登录失败");
        }
    }

    @Override
    public void refreshCheckWechatLogin(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            loginSuccess();
        }else {
            showToast("微信登录失败");
        }
    }

    @Override
    public void onBackPressed() {
        if(loginFragment3.isShowing()){
            loginFragment3.hide();
            loginFragment2.show(false);
        }else if(loginFragment2.isShowing()){
            loginFragment2.hide(true);
            loginFragment1.show(true);
        }else if(loginFragment1.isShowing()){
            super.onBackPressed();
        }
    }

    @Override
    public void refreshLogin(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            loginSuccess();
        }else {
//            showToast("登录失败");
        }
    }

    @Override
    public void innerNetworkRefresh(boolean success) {

    }

    //------------------------------------------------------------------动态申请权限
    private static final int RC_SETTINGS_SCREEN = 521;//设置页返回
    public static final int RC_RECEIVER_READ_SMS_PERM = 127;//摄像头、sdcard读写权限

    private final String[] receiver_read_sms = { Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS };

    @AfterPermissionGranted(RC_RECEIVER_READ_SMS_PERM)
    public void readSMSTask() {
        if (EasyPermissions.hasPermissions(this, receiver_read_sms)) {
            // Have permission, do the thing!
            initBroadcastReceiver();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_msg_contacts),RC_RECEIVER_READ_SMS_PERM, receiver_read_sms);
        }
    }

}
