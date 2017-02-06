package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/1/23
 * @descript 主页viewmodel
 */
public interface IMainViewModel extends ICPBaseBoostViewModel {
    public void refreshVersionCheck(RequestFreshStatus status);
    public void refreshRequestAimType(RequestFreshStatus status);
}
