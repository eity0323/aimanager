package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.sien.aimanager.R;
import com.sien.aimanager.control.BmobUtil;
import com.sien.aimanager.model.ISettingViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBasePresenter;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.databmob.beans.PersonSetBean;
import com.sien.lib.databmob.beans.UserResult;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

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

    private UserResult userInfoVO;

    public SettingPresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (ISettingViewModel)context;
    }

    @Override
    public void onStart() {

    }

    public UserResult getUserInfoVO() {
        return userInfoVO;
    }

    /**
     * 请求用户详情
     */
    public void requestUserInfo(String userName){
        if (!TextUtils.isEmpty(userName)) {
            BmobUtil.requestUserInfo(userName);
        }
    }

    public List<PersonSetBean> getSettingDatas(){
        List<PersonSetBean> list = new ArrayList<>();

        list.add(new PersonSetBean(R.mipmap.ic_settings_share , "分享给朋友",null));
//        list.add(new PersonSetBean(R.mipmap.ic_settings_suggest , "意见反馈",null));
        list.add(new PersonSetBean(R.mipmap.ic_settings_about , "关于",null));

        return list;
    }

    @Subscribe
    public void UserInfoEventReceiver(PersonalEvents.UserInfoEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_REQUESTUSERINFO);
        }
    }

    @Subscribe
    public void LoginEventReceiver(PersonalEvents.LoginEvent event) {
        if (event != null){
            if (event.checkStatus()){
                UserResult user = (UserResult)event.getData();

                if (user != null) {
                    userInfoVO = user;

                    //登录
                    impl.refreshUserInfo(RequestFreshStatus.REFRESH_SUCCESS);
                }else {
                    impl.refreshUserInfo(RequestFreshStatus.REFRESH_NODATA);
                }
            }else {
                impl.refreshUserInfo(RequestFreshStatus.REFRESH_ERROR);
            }
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
                List<UserResult> result = (List<UserResult>)msg.obj;
                if (CollectionUtils.IsNullOrEmpty(result)){
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
