package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/9/30
 * @descript 登录viewmodel
 */
public interface ILoginViewModel extends ICPBaseBoostViewModel {
    /*登录fragment切换*/
    public void refreshFragmentChange(RequestFreshStatus status, int fragmentIndex);
    /*请求登录*/
    public void refreshLogin(RequestFreshStatus status);
    /*校验微信登录*/
    public void refreshWechatLoginStatus(RequestFreshStatus status, String code);
    /*微信登录状态*/
    public void refreshCheckWechatLogin(RequestFreshStatus status);
}
