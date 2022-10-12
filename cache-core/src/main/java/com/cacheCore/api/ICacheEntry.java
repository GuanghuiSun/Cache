package com.cacheCore.api;

/**
 * 缓存明细信息
 *
 * @author sgh
 * @date 2022/10/11 10:47
 */
public interface ICacheEntry<K, V> {

    /**
     * 键
     *
     * @return key
     */
    K key();

    /**
     * 值
     *
     * @return value
     */
    V value();
}
