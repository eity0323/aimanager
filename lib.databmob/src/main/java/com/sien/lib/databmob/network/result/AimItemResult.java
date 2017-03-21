package com.sien.lib.databmob.network.result;

import com.sien.lib.databmob.beans.AimItemVO;
import com.sien.lib.databmob.beans.BaseResult;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/4
 * @descript 目标记录项返回结果
 */
public class AimItemResult extends BaseResult {
    private List<AimItemVO> data;

    public List<AimItemVO> getData() {
        return data;
    }

    public void setData(List<AimItemVO> data) {
        this.data = data;
    }
}
