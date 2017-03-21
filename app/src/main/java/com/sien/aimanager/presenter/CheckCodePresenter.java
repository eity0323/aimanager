package com.sien.aimanager.presenter;

import android.os.Message;

import com.sien.aimanager.activity.LoginActivity;
import com.sien.aimanager.control.BmobUtil;
import com.sien.aimanager.model.ICheckCodeViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 验证码处理类
 */
public class CheckCodePresenter extends BusBasePresenter {

    private ICheckCodeViewModel impl;

    public CheckCodePresenter(LoginActivity context, ICheckCodeViewModel impl){
        updateMessageHander = new BasePresenter.InnerHandler(this);

        mcontext = context;

        this.impl = impl;
    }

    public void requestCheckCode(String mobile){
        BmobUtil.requestSMSCode(mobile);
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void MsgCodeEventReceiver(PersonalEvents.MsgCodeEvent event){
        if (event != null){
            if (event.checkStatus()){
                impl.refreshMsgCode(RequestFreshStatus.REFRESH_SUCCESS);
            }else {
                impl.refreshMsgCode(RequestFreshStatus.REFRESH_ERROR);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        CheckCodePresenter theActivity = (CheckCodePresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }
}
