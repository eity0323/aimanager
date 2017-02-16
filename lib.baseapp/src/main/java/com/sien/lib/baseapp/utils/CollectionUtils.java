package com.sien.lib.baseapp.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 列表工具类
 */
public class CollectionUtils {
    public static <T> boolean IsNullOrEmpty(Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static boolean IsNullOrEmpty(Map list) {
        return list == null || list.isEmpty();
    }
}
