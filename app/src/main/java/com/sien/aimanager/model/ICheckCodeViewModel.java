package com.sien.aimanager.model;

import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 验证码viewmodel
 */
public interface ICheckCodeViewModel {
    /*请求短信验证码*/
    public void refreshMsgCode(RequestFreshStatus status);
}
