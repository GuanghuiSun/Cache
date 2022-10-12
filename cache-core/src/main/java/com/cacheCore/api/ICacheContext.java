package com.cacheCore.api;


import java.util.Map;

/**
 * 缓存上下文
 *
 * @author sgh
 * @date 2022/10/11 10:07
 */
public interface ICacheContext<K, V> {

    /**
     * map 信息
     * @return map
     */
    Map<K, V> map();

    /**
     * 大小限制
     * @return 大小限制
     */
    int size();


}
