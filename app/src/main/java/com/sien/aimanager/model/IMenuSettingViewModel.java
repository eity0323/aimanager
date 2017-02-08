package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;

/**
 * @author sien
 * @date 2016/12/30
 * @descript 设置viewmodel
 */
public interface IMenuSettingViewModel extends ICPBaseBoostViewModel{
    public void refreshVersionCheck(boolean success);
}
