package com.sien.lib.datapp.network.request;

/**
 * @author sien
 * @date 2016/10/18
 * @descript 反馈请求参数
 */
public class FeedbackRequest {
    private String appPlat;//终端平台（暂未使用，写死"android"）
    private String appVersion;//应用版本号 eg：1.0.0
    private String content;
    private String icon;
    private String status;
    private String terminalOs;//终端系统版本号 （操作系统） eg：android 4.1.2
    private String terminalType;//终端型号 （终端机型） eg：三星Note4
    private String userId;

    public String getAppPlat() {
        return appPlat;
    }

    public void setAppPlat(String appPlat) {
        this.appPlat = appPlat;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerminalOs() {
        return terminalOs;
    }

    public void setTerminalOs(String terminalOs) {
        this.terminalOs = terminalOs;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
