package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 新建目标分类viewmodel
 */
public interface INewAimTypeViewModel extends ICPBaseBoostViewModel{
    public void refreshInsertAimType(RequestFreshStatus status);
}
