package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.IAimTypeListViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.VersionCheckVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.action.MainRequestAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;
import com.sien.lib.datapp.network.result.VersionCheckResult;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/1/23
 * @descript 目标分类列表presenter
 */
public class AimTypeListPresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_VERSIONCHECK = 1;//版本校验
    private final int MSG_UPDATE_AIMTYPE = 2;//查询目标类型

    private IAimTypeListViewModel impl;

    private VersionCheckVO versionCheckVO;
    private List<AimTypeVO> datasource;

    public AimTypeListPresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);
        impl = (IAimTypeListViewModel) context;
    }

    @Override
    public ICPBaseBoostViewModel createViewModel() {
        return impl;
    }

    public void requestVersionCheck(){
        MainRequestAction.requestVersionCheck(mcontext);
    }

    public void requestAimTypeDatas(){
        MainDatabaseAction.requestAimTypeFixedDatas(mcontext);
    }

    public void deleteAimType(Long aimTypeId){
        MainDatabaseAction.deleteAimTypeById(mcontext,aimTypeId);
    }

    public VersionCheckVO getVersionCheckVO() {
        return versionCheckVO;
    }

    public List<AimTypeVO> getDatasource() {
        if (datasource == null){
            datasource = new ArrayList<>();
        }
        return datasource;
    }

    @Subscribe
    public void RequestVersionCheckEventReceiver(DatappEvent.RequestVersionCheckEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_VERSIONCHECK);
        }
    }

    @Subscribe
    public void AimTypeEventReceiver(DatappEvent.AimTypeEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_AIMTYPE);
        }
    }

    @Subscribe
    public void deleteAimTypeEventReceiver(DatappEvent.deleteAimTypeEvent event){
        if (event != null){
            if (event.checkStatus()){
                //删除成功，刷新数据
                requestAimTypeDatas();
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null) return;

        AimTypeListPresenter theActivity = (AimTypeListPresenter) helper;
        if (theActivity == null)    return;

        if (impl == null)   return;

        if (msg.what == MSG_UPDATE_VERSIONCHECK){
            if (msg.obj != null){
                VersionCheckResult result = (VersionCheckResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    List<VersionCheckVO> list = result.getData();
                    if (list != null && list.size() > 0){
                        versionCheckVO = list.get(0);

                        impl.refreshVersionCheck(RequestFreshStatus.REFRESH_SUCCESS);
                        return;
                    }
                }
            }
            impl.refreshVersionCheck(RequestFreshStatus.REFRESH_ERROR);
        }else if (msg.what == MSG_UPDATE_AIMTYPE){
            if (msg.obj != null){
                List<AimTypeVO> list = (List<AimTypeVO>) msg.obj;
                if (list != null && list.size() > 0 ){
                    datasource = list;
                    impl.refreshRequestAimType(RequestFreshStatus.REFRESH_SUCCESS);
                    return;
                }
                impl.refreshRequestAimType(RequestFreshStatus.REFRESH_NODATA);
                return;
            }
            impl.refreshRequestAimType(RequestFreshStatus.REFRESH_ERROR);
        }
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();

        versionCheckVO = null;
        impl = null;

        if (datasource != null){
            datasource.clear();
        }
        datasource = null;
    }
}
