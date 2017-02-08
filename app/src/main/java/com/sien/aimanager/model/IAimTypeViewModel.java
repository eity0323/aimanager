package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 目标分类管理viewmodel
 */
public interface IAimTypeViewModel extends ICPBaseBoostViewModel{
    public void refreshAimItem(RequestFreshStatus status);
}
