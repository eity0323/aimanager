package com.sien.aimanager.presenter;

import android.app.Activity;
import android.os.Message;

import com.sien.aimanager.model.IFeedbackViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.beans.UploadImageVO;
import com.sien.lib.databmob.events.DatappEvent;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.network.result.UploadImageResult;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/10/18
 * @descript 反馈处理类
 */
public class FeedbackPresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_UPLOADIMAGE = 1;//上传图片
    private final int MSG_UPDATE_FEEDBACK = 2;//添加反馈

    private IFeedbackViewModel impl;

    private ArrayList<String> list;

    private String uploadPhotoUrls = "";

    public FeedbackPresenter(Activity context){
        updateMessageHander = new InnerHandler(this);

        mcontext = context;
        impl = (IFeedbackViewModel) context;
    }

    public ArrayList<String> getPhotosPathExceptHolder(){
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void removePhotoPath(String path){
        if(list.size() > 0) {
            list.remove(path);
        }
    }

    public void setPhotosPath(ArrayList<String> datas){
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        list.addAll(datas);
    }

    public void addPhotoPath(String path){
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(0, path);
    }


    public String getUploadPhotoUrls(){
        if (uploadPhotoUrls.endsWith(",")){
            uploadPhotoUrls = uploadPhotoUrls.substring(0,uploadPhotoUrls.length()- 1);
        }
        return uploadPhotoUrls;
    }

    @Override
    public void onStart() {

    }

    @Subscribe
    public void FeedbackEventReceiver(DatappEvent.FeedbackEvent event){
        if (event != null){
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_FEEDBACK;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Subscribe
    public void UploadImageEventReceiver(DatappEvent.UploadImageEvent event){
        if (event != null){
            if (updateMessageHander != null) {
                Message msg = new Message();
                msg.what = MSG_UPDATE_UPLOADIMAGE;
                msg.obj = event.getResult();
                updateMessageHander.sendMessage(msg);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        FeedbackPresenter theActivity = (FeedbackPresenter) helper;
        if (theActivity == null)
            return;

        if (impl == null){
            //暂不处理
            return;
        }

        if (msg.what == MSG_UPDATE_UPLOADIMAGE){
            if (msg.obj != null){
                UploadImageResult result = (UploadImageResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    List<UploadImageVO> pathlist = result.getData();
                    uploadPhotoUrls = "";
                    if (pathlist != null && pathlist.size() > 0){
                        for (UploadImageVO str: pathlist) {
                            uploadPhotoUrls += str.getFileName() + ",";
                        }
                        impl.refreshUploadImage(RequestFreshStatus.REFRESH_SUCCESS);
                        return;
                    }
                    impl.refreshUploadImage(RequestFreshStatus.REFRESH_NODATA);
                    return;
                }
            }
            impl.refreshUploadImage(RequestFreshStatus.REFRESH_ERROR);
        }else if (msg.what == MSG_UPDATE_FEEDBACK){
            if (msg.obj != null){
                BaseResult result = (BaseResult)msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    impl.refreshFeedback(RequestFreshStatus.REFRESH_SUCCESS);
                    return;
                }
            }
            impl.refreshFeedback(RequestFreshStatus.REFRESH_ERROR);
        }
    }

    @Override
    public void releaseMemory(){
        super.releaseMemory();
        if (list != null){
            list.clear();
        }
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }

    @Override
    public IFeedbackViewModel createViewModel() {
        return impl;
    }
}
