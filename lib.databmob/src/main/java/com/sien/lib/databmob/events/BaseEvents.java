package com.sien.lib.databmob.events;

/**
 * @author sien
 * @date 2016/9/14
 * @descript 基类事件
 */
public class BaseEvents {
    public final static int STATUS_SUCCESS = 1;
    public final static int STATUS_FAIL_NETERROR = 2;
    public final static int STATUS_FAIL_OHTERERROR = 3;

    protected int status;
    protected Object data;

    public BaseEvents(int status, Object data){
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public boolean checkStatus() {
        return status == STATUS_SUCCESS;
    }

    public Object getData() {
        return data;
    }

}
