package com.sien.lib.datapp.beans;


/**
 * @author sien
 * @date 2016/9/2
 * @descript 个人中心设置实体
 */
public class PersonSetBean extends CPBaseVO {
    private int resourceId;
    private String descript;
    private String resourceUrl;

    public PersonSetBean() {
    }

    public PersonSetBean(int resourceId, String descript, String resourceUrl) {
        this.resourceId = resourceId;
        this.descript = descript;
        this.resourceUrl = resourceUrl;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
