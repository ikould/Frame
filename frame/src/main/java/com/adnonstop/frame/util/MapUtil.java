package com.adnonstop.frame.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Map 相关工具类
 *
 * @author ikould on 2018/2/11.
 */

public class MapUtil {

    /**
     * 使用Map按key进行排序
     *
     * @param map Map
     * @return 排序后的Map
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map, Comparator mapKeyComparator) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        if (mapKeyComparator == null)
            mapKeyComparator = new MapKeyComparator();
        Map<String, Object> sortMap = new TreeMap<>(mapKeyComparator);
        sortMap.putAll(map);
        return sortMap;
    }

    // 排序比较
    public static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }
}
