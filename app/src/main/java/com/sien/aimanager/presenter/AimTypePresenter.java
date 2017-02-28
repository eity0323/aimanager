package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.IAimTypeViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 目标分类管理presenter
 */
public class AimTypePresenter extends BusBaseBoostPresenter{
    private final int MSG_UPDATE_AIMITEM = 1;//请求目标记录
    private final int MSG_UPDATE_AIMTYPE = 2;//目标分类更新，请求最新数据
    private IAimTypeViewModel impl;
    private Long mAimTypeId;

    private List<AimItemVO> datasource;

    public AimTypePresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);
        impl = (IAimTypeViewModel) context;
    }

    public List<AimItemVO> getDatasource() {
        if (datasource == null){
            datasource = new ArrayList<>();
        }
        return datasource;
    }

    public void requestAimItemDatas(Long aimTypeId){
        mAimTypeId = aimTypeId;
        MainDatabaseAction.requestAimItemDatas(mcontext,aimTypeId);
    }

    /*分类数据变更，请求新数据*/
    public void requestAimTypeDatas(Long aimTypeId){
        MainDatabaseAction.requestAimTypeDatasById(mcontext,aimTypeId);
    }

    public void deleteAimItem(Long aimItemId){
        MainDatabaseAction.deleteAimItemById(mcontext,aimItemId);
    }

    public void modifyAimItem(AimItemVO aimItemVO){
        MainDatabaseAction.insertOrReplaceAimItem(mcontext,aimItemVO);
    }

    public void insertOrReplaceAimItem(AimItemVO aimItemVO){
        MainDatabaseAction.insertOrReplaceAimItem(mcontext,aimItemVO);
    }

    public void updateAimTypeStatusById(Long aimTypeId,float percent){
        MainDatabaseAction.updateAimTypeStatus(mcontext,aimTypeId,percent);
    }

    @Subscribe
    public void AimItemEventReceiver(DatappEvent.AimItemEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_AIMITEM);
        }
    }

    @Subscribe
    public void AimTypeEventReceiver(DatappEvent.AimTypeEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_AIMTYPE);
        }
    }

    @Subscribe
    public void deleteAimItemEventReceiver(DatappEvent.deleteAimItemEvent event){
        if (event != null){
            if (event.checkStatus()){
                //删除成功，刷新数据
                requestAimItemDatas(mAimTypeId);
            }
        }
    }

    @Subscribe
    public void insertAimTypeEventReceiver(DatappEvent.insertAimTypeEvent event){
        if (event != null){
            if (event.checkStatus()){
                //新增 or 更新成功，刷新数据
                requestAimItemDatas(mAimTypeId);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null) return;
        if (impl == null) return;

        AimTypePresenter theActivity = (AimTypePresenter) helper;
        if (theActivity == null) return;

        if (msg.what == MSG_UPDATE_AIMITEM){
            datasource = (List<AimItemVO>) msg.obj;
            if (datasource != null && datasource.size() > 0){
                impl.refreshAimItem(RequestFreshStatus.REFRESH_SUCCESS);
                return;
            }
            impl.refreshAimItem(RequestFreshStatus.REFRESH_NODATA);
        }else if (msg.what == MSG_UPDATE_AIMTYPE){
            List<AimTypeVO> list = (List<AimTypeVO>) msg.obj;
            if (!CollectionUtils.IsNullOrEmpty(list)){
                impl.refreshAimType(RequestFreshStatus.REFRESH_SUCCESS,list.get(0));
                return;
            }
            impl.refreshAimType(RequestFreshStatus.REFRESH_NODATA,null);
        }
    }

    @Override
    public IAimTypeViewModel createViewModel() {
        return impl;
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();

        if (datasource != null){
            datasource.clear();
        }
        datasource = null;

        impl = null;
    }
}
