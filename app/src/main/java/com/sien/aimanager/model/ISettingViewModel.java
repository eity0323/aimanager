package com.sien.aimanager.model;

import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 设置viewmodel
 */
public interface ISettingViewModel {
    public void refreshUserInfo(RequestFreshStatus status);
    public void refreshLogout(RequestFreshStatus status);
}
