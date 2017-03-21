package com.sien.lib.databmob.events;

import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.network.result.LoginResult;

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
