package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.R;
import com.sien.aimanager.model.ISettingViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.datapp.beans.PersonSetBean;
import com.sien.lib.datapp.beans.UserInfoVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 设置presenter
 */
public class SettingPresenter extends BusBasePresenter {
    private final int MSG_UPDATE_REQUESTUSERINFO = 1;//更新用户信息

    private ISettingViewModel impl;

    private UserInfoVO userInfoVO;

    public SettingPresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (ISettingViewModel)context;
    }

    @Override
    public void onStart() {

    }

    public UserInfoVO getUserInfoVO() {
        return userInfoVO;
    }

    /**
     * 请求用户详情
     */
    public void requestUserInfo(){
        MainDatabaseAction.requestUserInfoDatas(mcontext);
    }

    public List<PersonSetBean> getSettingDatas(){
        List<PersonSetBean> list = new ArrayList<>();

        list.add(new PersonSetBean(R.mipmap.ic_settings_share , "分享给朋友",null));
        list.add(new PersonSetBean(R.mipmap.ic_settings_suggest , "意见反馈",null));
        list.add(new PersonSetBean(R.mipmap.ic_settings_about , "关于",null));

        return list;
    }

    @Subscribe
    public void UserInfoEventReceiver(DatappEvent.UserInfoEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_REQUESTUSERINFO);
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        SettingPresenter theActivity = (SettingPresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }

        if (msg.what == MSG_UPDATE_REQUESTUSERINFO){
            if (msg.obj != null){
                List<UserInfoVO> result = (List<UserInfoVO>)msg.obj;
                if (result != null && result.size() > 0){
                    userInfoVO = result.get(0);
                    impl.refreshUserInfo(RequestFreshStatus.REFRESH_SUCCESS);
                    return;
                }
            }
            impl.refreshUserInfo(RequestFreshStatus.REFRESH_NODATA);
        }
    }

    @Override
    public void releaseMemory(){
        super.releaseMemory();
        userInfoVO = null;
        impl = null;
    }
}
