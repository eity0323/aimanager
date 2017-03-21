package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.beans.AimTypeVO;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 目标分类管理viewmodel
 */
public interface IAimTypeViewModel extends ICPBaseBoostViewModel{
    /*更新目标项*/
    public void refreshAimItem(RequestFreshStatus status);
    /*更新目标分类*/
    public void refreshAimType(RequestFreshStatus status,AimTypeVO aimTypeVO);
}
