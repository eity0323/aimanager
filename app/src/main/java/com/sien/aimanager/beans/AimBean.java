package com.sien.aimanager.beans;

import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.beans.CPBaseVO;

import java.util.List;

/**
 * @author sien
 * @date 2017/2/15
 * @descript 目标展示实体
 */
public class AimBean extends CPBaseVO {
    public AimTypeVO aimTypeVO;//目标分类
    public List<AimItemVO> aimItemVOList;//目标项
}
