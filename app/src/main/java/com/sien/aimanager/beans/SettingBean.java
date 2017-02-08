package com.sien.aimanager.beans;

import com.sien.lib.datapp.beans.CPBaseVO;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 设置实体
 */
public class SettingBean extends CPBaseVO {
    private String title;
    private String value;
    private String icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
