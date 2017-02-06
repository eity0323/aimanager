package com.sien.lib.datapp.beans;

/**
 * @author sien
 * @date 2016/12/8
 * @descript 校验版本
 */
public class VersionCheckVO extends CPBaseVO {
    private int versionId;
    private String appType;
    private String versionNumber;
    private String upgradeMode;
    private String fileId;
    private String link;
    private String status;
    private String remark;
    private String minUpgradeVersion;

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getUpgradeMode() {
        return upgradeMode;
    }

    public void setUpgradeMode(String upgradeMode) {
        this.upgradeMode = upgradeMode;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMinUpgradeVersion() {
        return minUpgradeVersion;
    }

    public void setMinUpgradeVersion(String minUpgradeVersion) {
        this.minUpgradeVersion = minUpgradeVersion;
    }
}
