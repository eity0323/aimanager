package com.sien.aimanager.control;

import com.sien.lib.datapp.beans.AimTypeVO;

import java.util.Comparator;

/**
 * @author sien
 * @date 2017/3/3
 * @descript 优先级排序比较
 */
public class PriorityComparator implements Comparator<AimTypeVO>{

    @Override
    public int compare(AimTypeVO lhs, AimTypeVO rhs) {
        if (lhs == null || rhs == null){
            return 0;
        }

        if (lhs.getPriority() == null || rhs.getPriority() == null){
            return 0;
        }

        if (lhs.getPriority() < rhs.getPriority()){
            return 1;
        }
        return -1;
    }
}
