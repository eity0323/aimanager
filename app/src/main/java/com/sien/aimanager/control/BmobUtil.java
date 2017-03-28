package com.sien.aimanager.control;

import com.sien.lib.databmob.beans.UserResult;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.utils.CPLogUtil;
import com.sien.lib.databmob.utils.EventPostUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
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
                    EventPostUtil.post(new PersonalEvents.UpdatePwdEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"更新失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 重置密码
     * @param checkCode
     */
    public static void resetPwd(String checkCode,String newPwd){
        BmobUser.resetPasswordBySMSCode(checkCode,newPwd, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    EventPostUtil.post(new PersonalEvents.ResetPwdEvent(PersonalEvents.STATUS_SUCCESS,"重置成功"));
                }else{
                    EventPostUtil.post(new PersonalEvents.ResetPwdEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 验证码登录
     * @param mobile
     * @param checkCode
     */
    public static void login(String mobile, String checkCode){
        BmobUser.loginBySMSCode(mobile, checkCode, new LogInListener<UserResult>() {

            @Override
            public void done(UserResult user, BmobException e) {
                if (user != null){
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_SUCCESS,user));
                }else {
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 密码登录
     * @param mobile
     * @param pwd
     */
    public static void loginByAccount(String mobile, String pwd){
        BmobUser.loginByAccount(mobile, pwd, new LogInListener<UserResult>() {

            @Override
            public void done(UserResult user, BmobException e) {
                if (user != null){
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_SUCCESS,user));
                }else {
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 注销
     */
    public static void logout(){
        BmobUser.logOut();
    }

    /**
     * 邮箱登录
     * @param email
     * @param pwd
     */
    public static void loginByEmailPwd(String email,String pwd){
        BmobUser.loginByAccount(email, pwd, new LogInListener<UserResult>() {

            @Override
            public void done(UserResult user, BmobException e) {
                if (user != null){
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_SUCCESS,user));
                }else {
                    EventPostUtil.post(new PersonalEvents.LoginEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 注册及登录
     * @param mobile
     * @param checkCode
     */
    public static void signOrLogin(String mobile, String checkCode){
        UserResult user = new UserResult();
        user.setPassword("123456");
        user.setMobilePhoneNumber(mobile);
        user.setAge(0);
        user.setSex(false);
        //添加String类型的数组--String数组支持去重
        user.addAllUnique("hobby", new ArrayList<String>());
        user.signOrLogin(checkCode, new SaveListener<UserResult>() {

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
                    if ("10010".equals(e.getErrorCode())){
                        EventPostUtil.post(new PersonalEvents.MsgCodeEvent(PersonalEvents.STATUS_FAIL_NETERROR, "数量限制"));
                    }else {
                        EventPostUtil.post(new PersonalEvents.MsgCodeEvent(PersonalEvents.STATUS_FAIL_OHTERERROR, "请求验证码失败：code =" + e.getErrorCode() + ",msg = " + e.getLocalizedMessage()));
                    }
                }
            }
        });
    }

    /**
     * 请求邮件验证
     */
    public static void requestEmailCode(final String email) {
        BmobUser.requestEmailVerify(email, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){//短信验证码已验证成功
                    EventPostUtil.post(new PersonalEvents.VerifyEmailCodeEvent(PersonalEvents.STATUS_SUCCESS,"请求验证邮件成功，请到" + email + "邮箱中进行激活账户。"));
                }else{
                    EventPostUtil.post(new PersonalEvents.VerifyEmailCodeEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"验证失败：code ="+e.getErrorCode()+",msg = "+e.getLocalizedMessage()));
                }
            }

        });
    }

    /**
     * 校验验证码
     * @param mobile
     * @param checkCode
     */
    public static void verifySMSCode(String mobile, String checkCode){
        BmobSMS.verifySmsCode(mobile, checkCode, new UpdateListener() {

            @Override
            public void done(BmobException ex) {
                if(ex==null){//短信验证码已验证成功
                    EventPostUtil.post(new PersonalEvents.VerifyMsgCodeEvent(PersonalEvents.STATUS_SUCCESS,"验证通过"));
                }else{
                    EventPostUtil.post(new PersonalEvents.VerifyMsgCodeEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,"验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 查询用户
     * @param userName
     */
    public static void requestUserInfo(String userName) {
        BmobQuery<UserResult> query = new BmobQuery<UserResult>();
        query.addWhereEqualTo("username", userName);
        query.findObjects(new FindListener<UserResult>() {

            @Override
            public void done(List<UserResult> object, BmobException e) {
                if (e == null){
                    EventPostUtil.post(new PersonalEvents.UserInfoEvent(PersonalEvents.STATUS_SUCCESS, object));
                }else {
                    EventPostUtil.post(new PersonalEvents.UserInfoEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                }
            }
        });
    }

    /**
     * 获取当前用户信息
     */
    public static void requestCurrentUserInfo(){
        UserResult result = requestCurrentUserInfoSync();
        List<UserResult> list = new ArrayList<>();
        list.add(result);
        EventPostUtil.post(new PersonalEvents.UserInfoEvent(PersonalEvents.STATUS_SUCCESS, list));
    }

    /**
     * 查询本地用户数据
     * @return
     */
    private static UserResult requestCurrentUserInfoSync(){
        String username = (String) BmobUser.getObjectByKey("username");
        Integer age = (Integer) BmobUser.getObjectByKey("age");
        Boolean sex = (Boolean) BmobUser.getObjectByKey("sex");
        JSONArray hobby= (JSONArray) BmobUser.getObjectByKey("hobby");

        UserResult result = new UserResult();
        result.setAge(age);
        result.setSex(sex);
        result.setUsername(username);
        if (hobby != null){
            List<String> hobbies = new ArrayList<>();
            for (int i =0,j = hobby.length();i<j;i++){
                hobbies.add(hobbies.get(i));
            }
            result.setHobby(hobbies);
        }

        return result;
    }

    /**
     * 更新用户信息
     */
    public static void updateUserInfo(UserResult userResult){
        UserResult bmobUser = BmobUser.getCurrentUser(UserResult.class);
        if (bmobUser != null) {
            final UserResult newUser = new UserResult();
            //-----------------------普通setter操作-------------------------------
            //number类型
            newUser.setAge(userResult.getAge());
            newUser.setSex(userResult.getSex());
            //添加String类型的数组--String数组支持去重
            newUser.addAllUnique("hobby", userResult.getHobby());
            newUser.update(bmobUser.getObjectId(), new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        UserResult result = requestCurrentUserInfoSync();
                        EventPostUtil.post(new PersonalEvents.UpdateUserInfoEvent(PersonalEvents.STATUS_SUCCESS, result));
                    } else {
                        EventPostUtil.post(new PersonalEvents.UpdateUserInfoEvent(PersonalEvents.STATUS_FAIL_OHTERERROR,e.getLocalizedMessage()));
                    }
                }
            });

//			//更新number
//			newUser.setValue("age",25);
//			//更新整个Object
//			newUser.setValue("banker",person);
//			//更新String数组
//			newUser.setValue("hobby",Arrays.asList("看书","游泳"));
        }
    }
}
