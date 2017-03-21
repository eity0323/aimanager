package com.sien.lib.databmob.network.result;

import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.beans.UploadImageVO;

import java.util.List;

/**
 * @author sien
 * @date 2016/10/18
 * @descript 上传图片返回结果
 */
public class UploadImageResult extends BaseResult {
    private List<UploadImageVO> data;

    public List<UploadImageVO> getData() {
        return data;
    }

    public void setData(List<UploadImageVO> data) {
        this.data = data;
    }
}
