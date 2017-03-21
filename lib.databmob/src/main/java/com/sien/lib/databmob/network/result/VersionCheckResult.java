package com.sien.lib.databmob.network.result;

import com.sien.lib.databmob.beans.BaseResult;
import com.sien.lib.databmob.beans.VersionCheckVO;

import java.util.List;

/**
 * @author sien
 * @date 2016/12/8
 * @descript 版本校验
 */
public class VersionCheckResult extends BaseResult {
    private List<VersionCheckVO> data;

    public List<VersionCheckVO> getData() {
        return data;
    }

    public void setData(List<VersionCheckVO> data) {
        this.data = data;
    }
}
