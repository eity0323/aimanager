package com.sien.lib.databmob.network.result;

import com.sien.lib.databmob.beans.AimTypeVO;
import com.sien.lib.databmob.beans.BaseResult;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/4
 * @descript 目标类型返回结果
 */
public class AimTypeResult extends BaseResult {
    private List<AimTypeVO> data;

    public List<AimTypeVO> getData() {
        return data;
    }

    public void setData(List<AimTypeVO> data) {
        this.data = data;
    }
}
