package com.sien.lib.databmob.events;

import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.beans.UserResult;
import com.sien.lib.databmob.network.result.LoginResult;

import java.util.List;

/**
 * @author sien
 * @date 2016/9/30
 * @descript 个人中心事件
 */
public class PersonalEvents {
    public final static int STATUS_SUCCESS = BaseEvents.STATUS_SUCCESS;
    public final static int STATUS_FAIL_NETERROR = BaseEvents.STATUS_FAIL_NETERROR;
    public final static int STATUS_FAIL_OHTERERROR = BaseEvents.STATUS_FAIL_OHTERERROR;

    /**
     * 获取验证码事件
     */
    public static class MsgCodeEvent extends BaseEvents{
        public MsgCodeEvent(int status,Object data){
            super(status, data);
        }

        public BaseResult getResult(){
            BaseResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }

    /**
     * 校验验证码事件
     */
    public static class VerifyMsgCodeEvent extends BaseEvents{
        public VerifyMsgCodeEvent(int status,Object data){
            super(status, data);
        }

        public BaseResult getResult(){
            BaseResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }

    /**
     * 校验验证码事件
     */
    public static class VerifyEmailCodeEvent extends BaseEvents{
        public VerifyEmailCodeEvent(int status,Object data){
            super(status, data);
        }

        public BaseResult getResult(){
            BaseResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }

    /**
     * 重置密码
     */
    public static class ResetPwdEvent extends BaseEvents{
        public ResetPwdEvent(int status,Object data){
            super(status, data);
        }

        public BaseResult getResult(){
            BaseResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }

    /**
     * 登录事件
     */
    public static class LoginEvent extends BaseEvents{
        public LoginEvent(int status,Object data){
            super(status, data);
        }

        public String getResult(){
            String response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (String)data;
            }

            return response;
        }
    }

    /**
     * 查询用户信息事件
     */
    public static class UserInfoEvent extends BaseEvents{
        public UserInfoEvent(int status,Object data){
            super(status, data);
        }

        public List<UserResult> getResult(){
            List<UserResult> response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<UserResult>)data;
            }

            return response;
        }
    }

    /**
     * 更新用户信息事件
     */
    public static class UpdateUserInfoEvent extends BaseEvents{
        public UpdateUserInfoEvent(int status,Object data){
            super(status, data);
        }

        public UserResult getResult(){
            UserResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (UserResult)data;
            }

            return response;
        }
    }

    /**
     * 更新密码事件
     */
    public static class UpdatePwdEvent extends BaseEvents{
        public UpdatePwdEvent(int status,Object data){
            super(status, data);
        }

        public String getResult(){
            String response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (String)data;
            }

            return response;
        }
    }

    /**
     * 微信登录事件
     */
    public static class WeChatLoginEvent extends BaseEvents{
        public WeChatLoginEvent(int status,Object data){
            super(status, data);
        }

        public LoginResult getResult(){
            LoginResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (LoginResult)data;
            }

            return response;
        }
    }

    /**
     * 添加反馈事件
     */
    public static class FeedbackEvent extends BaseEvents{
        public FeedbackEvent(int status,Object data){
            super(status, data);
        }

        public BaseResult getResult(){
            BaseResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }


}
