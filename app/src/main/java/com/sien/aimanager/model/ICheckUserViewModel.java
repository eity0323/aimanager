package com.sien.aimanager.model;


import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 校验登录用户viewmodel
 */
public interface ICheckUserViewModel {
    /*请求登录*/
    public void refreshLogin(RequestFreshStatus status);
    /*自动填写验证码*/
    public void refreshFillSMS(RequestFreshStatus status, String value);
    /*请求短信验证码*/
    public void refreshMsgCode(RequestFreshStatus status);
}
