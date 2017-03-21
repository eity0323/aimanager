package com.sien.aimanager.control;

import com.sien.aimanager.beans.UserResult;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.utils.CPLogUtil;
import com.sien.lib.databmob.utils.EventPostUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author sien
 * @date 2017/3/17
 * @descript com.sien.aimanager.control
 */
public class BmobUtil {
    /**
     * 更新密码
     * @param oldPwd
     * @param newPwd
     */
    public static void updatePwd(String oldPwd,String newPwd){
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    EventPostUtil.post(new PersonalEvents.UpdatePwdEvent(PersonalEvents.STATUS_SUCCESS,"更新成功"));
                }else {
                    EventPostUtil.post(new PersonalEvents.UpdatePwdEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"更新失败"));
                }
            }
        });
    }

    /**
     * 登录
     * @param mobile
     * @param checkCode
     */
    public static void login(String mobile, String checkCode){
        BmobUser.signOrLoginByMobilePhone(mobile, checkCode, new LogInListener<UserResult>() {
            @Override
            public void done(UserResult userResult, BmobException e) {
                if (userResult != null){
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_SUCCESS,userResult));
                }else {
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 请求验证码
     * @param mobile
     */
    public static void requestSMSCode(String mobile){
        BmobSMS.requestSMSCode(mobile, "目标管理", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){//验证码发送成功
                    CPLogUtil.logInfo("CheckCodePresenter", "短信id："+integer);
                    EventPostUtil.post(new PersonalEvents.MsgCodeEvent(PersonalEvents.STATUS_SUCCESS,"请求验证码成功"));
                }else {
                    EventPostUtil.post(new PersonalEvents.MsgCodeEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"请求验证码失败"));
                }
            }
        });
    }
}
