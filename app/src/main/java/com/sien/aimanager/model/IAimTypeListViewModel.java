package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/1/23
 * @descript 目标分类列表viewmodel
 */
public interface IAimTypeListViewModel extends ICPBaseBoostViewModel {
    public void refreshVersionCheck(RequestFreshStatus status);
    public void refreshRequestAimType(RequestFreshStatus status);
}
