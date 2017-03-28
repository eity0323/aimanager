package com.sien.lib.databmob.beans;

import org.greenrobot.greendao.annotation.*;

/**
 * 用户实体  deprecated
 */
@Entity
public class UserInfoVO extends CPBaseVO{

    @Id(autoincrement = true)
    private Long id;
    private String userName;
    private String userPwd;
    private String realName;
    private String avatar;
    private String mobile;
    private String email;
    private Boolean bindMobile;
    private Boolean bindEmail;
    private Boolean devicePwd;

    @Generated
    public UserInfoVO() {
    }

    public UserInfoVO(Long id) {
        this.id = id;
    }

    @Generated
    public UserInfoVO(Long id, String userName, String userPwd, String realName, String avatar, String mobile, String email, Boolean bindMobile, Boolean bindEmail, Boolean devicePwd) {
        this.id = id;
        this.userName = userName;
        this.userPwd = userPwd;
        this.realName = realName;
        this.avatar = avatar;
        this.mobile = mobile;
        this.email = email;
        this.bindMobile = bindMobile;
        this.bindEmail = bindEmail;
        this.devicePwd = devicePwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(Boolean bindMobile) {
        this.bindMobile = bindMobile;
    }

    public Boolean getBindEmail() {
        return bindEmail;
    }

    public void setBindEmail(Boolean bindEmail) {
        this.bindEmail = bindEmail;
    }

    public Boolean getDevicePwd() {
        return devicePwd;
    }

    public void setDevicePwd(Boolean devicePwd) {
        this.devicePwd = devicePwd;
    }

}
