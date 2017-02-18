package com.sien.lib.photopick.bean;

import java.io.Serializable;

/**
 * @author sien
 * @date 2017-01-20
 * @description 预览图片实体（内部使用）
 */
public class SketchBean implements Serializable {
    public int height;
    public int width;
    public int x;
    public int y;
    //        原图
    public String imageBigUrl;
    //        缩略图
    public String smallImageUrl;

    public void setUrl(SketchVO picUrlBean) {
        this.imageBigUrl = picUrlBean.imageBigUrl;
        this.smallImageUrl = picUrlBean.smallImageUrl;
    }
}
