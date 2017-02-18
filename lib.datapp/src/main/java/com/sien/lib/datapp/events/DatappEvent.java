package com.sien.lib.datapp.events;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.beans.UserInfoVO;
import com.sien.lib.datapp.network.result.UploadImageResult;
import com.sien.lib.datapp.network.result.VersionCheckResult;

import java.util.List;

/**
 * @author sien
 * @date 2016/9/2
 * @descript main事件
 */
public class DatappEvent {
    public final static int STATUS_SUCCESS = 1;
    public final static int STATUS_FAIL_NETERROR = 2;
    public final static int STATUS_FAIL_OHTERERROR = 3;

    /**
     * 目标分类数据
     */
    public static class AimTypeEvent{
        private int status;
        private Object data;

        public AimTypeEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimTypeVO> getResult(){
            List<AimTypeVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimTypeVO>)data;
            }

            return response;
        }
    }

    /**
     * 目标记录数据
     */
    public static class AimItemEvent{
        private int status;
        private Object data;
        public AimItemEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimItemVO> getResult(){
            List<AimItemVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimItemVO>)data;
            }

            return response;
        }
    }

    /**
     * 删除目标分类数据
     */
    public static class deleteAimTypeEvent{
        private int status;
        private Object data;

        public deleteAimTypeEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimTypeVO> getResult(){
            List<AimTypeVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimTypeVO>)data;
            }

            return response;
        }
    }

    /**
     * 删除目标记录项数据
     */
    public static class deleteAimItemEvent{
        private int status;
        private Object data;

        public deleteAimItemEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimItemVO> getResult(){
            List<AimItemVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimItemVO>)data;
            }

            return response;
        }
    }

    /**
     * 添加目标分类数据
     */
    public static class insertAimTypeEvent{
        private int status;
        private Object data;

        public insertAimTypeEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimTypeVO> getResult(){
            List<AimTypeVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimTypeVO>)data;
            }

            return response;
        }
    }

    /**
     * 添加目标记录项数据
     */
    public static class insertAimItemEvent{
        private int status;
        private Object data;

        public insertAimItemEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<AimItemVO> getResult(){
            List<AimItemVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<AimItemVO>)data;
            }

            return response;
        }
    }

    /**
     * 添加用户信息数据
     */
    public static class insertUserInfoEvent{
        private int status;
        private Object data;

        public insertUserInfoEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<UserInfoVO> getResult(){
            List<UserInfoVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<UserInfoVO>)data;
            }

            return response;
        }
    }


    /**
     * 查询用户信息
     */
    public static class UserInfoEvent{
        private int status;
        private Object data;

        public UserInfoEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public List<UserInfoVO> getResult(){
            List<UserInfoVO>  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (List<UserInfoVO>)data;
            }

            return response;
        }
    }

    /**
     * 请求版本校验事件
     */
    public static class RequestVersionCheckEvent{
        private int status;
        private Object data;
        public RequestVersionCheckEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public VersionCheckResult getResult(){
            VersionCheckResult  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (VersionCheckResult)data;
            }

            return response;
        }
    }

    /**
     * 添加反馈事件
     */
    public static class FeedbackEvent{
        private int status;
        private Object data;
        public FeedbackEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public BaseResult getResult(){
            BaseResult  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (BaseResult)data;
            }

            return response;
        }
    }

    /**
     * 上传图片事件
     */
    public static class UploadImageEvent{
        private int status;
        private Object data;

        public UploadImageEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public UploadImageResult getResult(){
            UploadImageResult response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (UploadImageResult)data;
            }

            return response;
        }
    }

    /**
     * 下载事件
     */
    public static class DownloadApkEvent{
        private int status;
        private Object data;
        public DownloadApkEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public String getResult(){
            String  response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = data.toString();
            }

            return response;
        }
    }

}
