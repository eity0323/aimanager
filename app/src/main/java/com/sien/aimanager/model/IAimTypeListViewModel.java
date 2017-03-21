package com.sien.aimanager.model;

import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2017/1/23
 * @descript 目标分类列表viewmodel
 */
public interface IAimTypeListViewModel extends ICPBaseBoostViewModel {
    /*检测更新版本*/
    public void refreshVersionCheck(RequestFreshStatus status);
    /*请求分类目标*/
    public void refreshRequestAimType(RequestFreshStatus status);
    /*删除分类目标*/
    public void refreshDelAimType(RequestFreshStatus status);
    /*校验页面有效性*/
    public boolean checkActiveStatus();
}
