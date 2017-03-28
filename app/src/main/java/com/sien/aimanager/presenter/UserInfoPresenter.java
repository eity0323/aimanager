package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.control.BmobUtil;
import com.sien.aimanager.model.IUserInfoViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.beans.UploadImageVO;
import com.sien.lib.databmob.beans.UserResult;
import com.sien.lib.databmob.events.BaseEvents;
import com.sien.lib.databmob.events.PersonalEvents;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.network.result.UploadImageResult;

import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/11/3
 * @descript 用户详情处理类
 */
public class UserInfoPresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_USERINFO = 1;//请求用户信息
    private final int MSG_UPDATE_UPDATEUSERINFO = 2;//更新用户信息
    private final int MSG_UPDATE_REQUESTUPLOADIMAGE = 3;//上传用户头像

    private IUserInfoViewModel impl;
    private UserResult datasource;

    public UserInfoPresenter(Context context){
        updateMessageHander = new InnerHandler(this);

        impl = (IUserInfoViewModel)context;

        mcontext = context;
    }

    public void requestUserInfoFromNetwork(){
        BmobUtil.requestCurrentUserInfo();
    }

    public UserResult getUserInfo(){
        return datasource;
    }

    public void updateUserInfo(UserResult request){
        BmobUtil.updateUserInfo(request);
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void RequestUserInfoEventReceiver(PersonalEvents.UserInfoEvent event){
        if (event != null){
            if (!serveStatusAvalible(event.getStatus())){
                //服务器开小差
                return;
            }

            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_USERINFO;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Subscribe
    public void UpdateUserInfoEventReceiver(PersonalEvents.UpdateUserInfoEvent event){
        if (event != null){
            if (!serveStatusAvalible(event.getStatus())){
                //服务器开小差
                return;
            }

            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_UPDATEUSERINFO;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

//    @Subscribe
//    public void UploadImageEventReceiver(PersonalEvents.UploadImageEvent event){
//        if (event != null){
//            if (!serveStatusAvalible(event.getStatus())){
//                //服务器开小差
//                return;
//            }
//
//            if (updateMessageHander != null) {
//                Message msg = new Message();
//                msg.what = MSG_UPDATE_REQUESTUPLOADIMAGE;
//                msg.obj = event.getResult();
//                updateMessageHander.sendMessage(msg);
//            }
//        }
//    }

    /*校验服务器有效性*/
    private boolean serveStatusAvalible(int status){
        boolean avalible = true;
        if (status == BaseEvents.STATUS_FAIL_NETERROR){
            impl.refreshServerError();
            avalible = false;
        }

        return avalible;
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        UserInfoPresenter theActivity = (UserInfoPresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }

        if (msg.what == MSG_UPDATE_USERINFO){
            if (msg.obj != null){
                List<UserResult> result = (List<UserResult>) msg.obj;
                if (CollectionUtils.IsNullOrEmpty(result)){
                    datasource = result.get(0);
                    impl.refreshUserInfo(RequestFreshStatus.REFRESH_SUCCESS);
                    return;
                }
            }
            impl.refreshUserInfo(RequestFreshStatus.REFRESH_ERROR);

        }else if (msg.what == MSG_UPDATE_UPDATEUSERINFO){
            if (msg.obj != null) {
                BaseResult result = (BaseResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    impl.refreshUpdateUserInfo(RequestFreshStatus.REFRESH_SUCCESS);
                    return;
                }
            }

            impl.refreshUpdateUserInfo(RequestFreshStatus.REFRESH_ERROR);
        }else if (msg.what == MSG_UPDATE_REQUESTUPLOADIMAGE){
            if (msg.obj != null){
                UploadImageResult result = (UploadImageResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    List<UploadImageVO> list = result.getData();
                    if (list != null && list.size() > 0){
                        impl.refresUploadImage(RequestFreshStatus.REFRESH_SUCCESS,list.get(0).getFileName());
                        return;
                    }
                    impl.refresUploadImage(RequestFreshStatus.REFRESH_NODATA,"");
                    return;
                }
            }
            impl.refresUploadImage(RequestFreshStatus.REFRESH_ERROR,"");
        }
    }

    @Override
    public void releaseMemory(){
        super.releaseMemory();
        datasource = null;
    }

    @Override
    public ICPBaseBoostViewModel createViewModel() {
        return impl;
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }
}
