package com.sien.lib.datapp.network.response;

/**
 * @author sien
 * @date 2016/10/14
 * @descript 登录返回数据
 */
public class LoginResponse {
    public static final int USER_TYPE_COMMON = 1;//普通用户
    public static final int USER_TYPE_EDITOR = 2;//编辑
    /**
     * 1、普通用户
     * 2、主编用户
     */
    private String userType;
    private String userName;
    private String photo;
    private String mobile;
    private String accountId;


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
