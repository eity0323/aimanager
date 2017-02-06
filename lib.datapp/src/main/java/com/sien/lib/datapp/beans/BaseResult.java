package com.sien.lib.datapp.beans;

import java.io.Serializable;

/**
 * @author sien
 * @date 2016/10/17
 * @descript 基类请求返回结果
 */
public class BaseResult implements Serializable{
    /**
     * 0、成功
     * 1、失败
     */
    private Integer returncode;
    // 用户类型
    private String message;

    public Integer getReturncode() {
        return returncode;
    }

    public void setReturncode(Integer returncode) {
        this.returncode = returncode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean checkRequestSuccess(){
        if (returncode == 0){
            return true;
        }
        return false;
    }
}
