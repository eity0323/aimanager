package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/11/3
 * @descript 用户详情viewmodel
 */
public interface IUserInfoViewModel extends ICPBaseBoostViewModel {
    /*请求用户信息*/
    public void refreshUserInfo(RequestFreshStatus status);
    /*更新用户信息*/
    public void refreshUpdateUserInfo(RequestFreshStatus status);
    /*上传用户头像*/
    public void refresUploadImage(RequestFreshStatus status, String filePath);
    /*服务器异常*/
    public void refreshServerError();
}
