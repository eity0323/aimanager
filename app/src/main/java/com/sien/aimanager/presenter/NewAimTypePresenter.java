package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.INewAimTypeViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 新建目标分类presenter
 */
public class NewAimTypePresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_INSERTAIMTYPE = 1;//添加目标分类

    private INewAimTypeViewModel impl;

    public NewAimTypePresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (INewAimTypeViewModel) context;
    }

    public void insertOrReplaceAimTypeRecord(AimTypeVO aimTypeVO){
        MainDatabaseAction.insertOrReplaceAimType(mcontext,aimTypeVO);
    }

    public void searchAimTypeRecord(){
        MainDatabaseAction.requestAimTypeFixedDatas(mcontext);
    }

    @Subscribe
    public void insertAimTypeEventReceiver(DatappEvent.insertAimTypeEvent event){
        if (event != null){
            if (updateMessageHander != null){
                updateMessageHander.sendEmptyMessage(MSG_UPDATE_INSERTAIMTYPE);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null) return;

        if (impl == null)   return;

        NewAimTypePresenter theActivity = (NewAimTypePresenter) helper;
        if (theActivity == null)    return;

        if (msg.what == MSG_UPDATE_INSERTAIMTYPE){
            impl.refreshInsertAimType(RequestFreshStatus.REFRESH_SUCCESS);
        }
    }

    @Override
    public ICPBaseBoostViewModel createViewModel() {
        return impl;
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();

        impl = null;
    }
}
