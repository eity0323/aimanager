package com.sien.lib.datapp.network.result;

import com.sien.lib.datapp.beans.BaseResult;
import com.sien.lib.datapp.network.response.LoginResponse;

import java.util.List;

/**
 * @author sien
 * @date 2016/9/30
 * @descript 登录请求结果
 */
public class LoginResult extends BaseResult {

    private List<LoginResponse> data;

    public List<LoginResponse> getData() {
        return data;
    }

    public void setData(List<LoginResponse> data) {
        this.data = data;
    }

}
