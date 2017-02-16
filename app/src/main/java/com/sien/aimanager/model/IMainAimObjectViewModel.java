package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/2/13
 * @descript aimobject版主页viewmodel
 */
public interface IMainAimObjectViewModel extends ICPBaseBoostViewModel {
    public void refreshRequestAimBean(RequestFreshStatus status);
}
