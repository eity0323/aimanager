package com.sien.aimanager.model;


import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/10/18
 * @descript 反馈viewmodel
 */
public interface IFeedbackViewModel extends ICPBaseBoostViewModel{
    public void refreshFeedback(RequestFreshStatus status);
    public void refreshUploadImage(RequestFreshStatus status);
    public void chooseImageFromAlbum();
    public void review(int position);
}
