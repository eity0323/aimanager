package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.IMenuSettingViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.datapp.beans.VersionCheckVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainRequestAction;
import com.sien.lib.datapp.network.result.VersionCheckResult;

import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/12/30
 * @descript 隐藏设置页
 */
public class MenuSettingPresenter extends BusBasePresenter {
    private final int MSG_UPDATE_VERSIONCHECK = 1;//版本校验

    private IMenuSettingViewModel impl;

    private VersionCheckVO versionCheckVO;

    public MenuSettingPresenter(Context context){
        mcontext = context;
        impl = (IMenuSettingViewModel)context;

        updateMessageHander = new InnerHandler(this);
    }

    public VersionCheckVO getVersionCheckVO() {
        return versionCheckVO;
    }

    public void requestCheckVersion(){
        MainRequestAction.requestVersionCheck(mcontext);
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void RequestVersionCheckEventReceiver(DatappEvent.RequestVersionCheckEvent event){
        if (event != null){
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_VERSIONCHECK;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null){
            return;
        }

        if (msg.what == MSG_UPDATE_VERSIONCHECK){
            if (msg.obj != null){
                VersionCheckResult result = (VersionCheckResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    List<VersionCheckVO> list = result.getData();
                    if (list != null && list.size() > 0){
                        versionCheckVO = list.get(0);

                        impl.refreshVersionCheck(true);
                        return;
                    }
                }
            }
            impl.refreshVersionCheck(false);
        }
    }
}
