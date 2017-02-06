package com.sien.lib.baseapp.events;

import com.sien.lib.baseapp.beans.APPOperateEventBundle;

/**
 * @author sien
 * @date 2016/10/12
 * @descript 基类触发事件
 */
public class BaseAppEvents {
    public final static int STATUS_SUCCESS = 1;
    public final static int STATUS_FAIL_NETERROR = 2;
    public final static int STATUS_FAIL_OHTERERROR = 3;

    /**
     * 权限设置事件
     */
    public static class PermissionSettingEvent{
        public final static int STATUS_SUCCESS = 1;
        public final static int STATUS_FAIL_NETERROR = 2;
        public final static int STATUS_FAIL_OHTERERROR = 3;

        private int status;
        private Object data;
        public PermissionSettingEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public int getResult(){
            int response = -1;
            if (status == STATUS_SUCCESS && data != null){
                response = Integer.valueOf(data.toString());
            }

            return response;
        }
    }

    /**
     * 应用操作事件
     */
    public static class APPOperateEvent{
        private int status;
        private Object data;
        public APPOperateEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public APPOperateEventBundle getResult(){
            APPOperateEventBundle response = null;
            if (status == STATUS_SUCCESS && data != null){
                response = (APPOperateEventBundle)data;
            }

            return response;
        }
    }

    /**
     * 网络状态变化
     */
    public static class NetworkChangeEvent{
        private int status;
        private Object data;
        public NetworkChangeEvent(int status,Object data){
            this.status = status;
            this.data = data;
        }

        public boolean checkStatus() {
            return status == STATUS_SUCCESS;
        }

        public Object getData() {
            return data;
        }

        public int getResult(){
            int  response = -1;
            if (status == STATUS_SUCCESS && data != null){
                response = Integer.valueOf(data.toString());
            }

            return response;
        }
    }
}
