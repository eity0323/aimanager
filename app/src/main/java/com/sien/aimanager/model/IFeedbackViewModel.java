package com.sien.aimanager.model;


import com.sien.lib.datapp.network.base.RequestFreshStatus;

/**
 * @author sien
 * @date 2016/10/18
 * @descript 反馈viewmodel
 */
public interface IFeedbackViewModel {
    public void refreshFeedback(RequestFreshStatus status);
    public void refreshUploadImage(RequestFreshStatus status);
    public void chooseImageFromAlbum();
    public void review(int position);
}
