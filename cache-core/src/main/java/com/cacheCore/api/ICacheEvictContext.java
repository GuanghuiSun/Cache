package com.cacheCore.api;

/**
 * 驱除策略上下文
 *
 * @author sgh
 * @date 2022/10/11 10:51
 */
public interface ICacheEvictContext<K, V> {

    /**
     * 新加的key
     *
     * @return key
     */
    K key();

    /**
     * cache 实现
     *
     * @return map
     */
    ICache<K, V> cache();

    /**
     * 获取大小
     *
     * @return 大小
     */
    int size();
}
