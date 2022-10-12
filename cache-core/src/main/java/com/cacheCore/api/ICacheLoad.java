package com.cacheCore.api;

/**
 * 缓存加载接口
 *
 * @author sgh
 * @date 2022/10/12 9:22
 */
public interface ICacheLoad<K, V> {

    /**
     * 加载缓存信息
     *
     * @param cache 缓存
     */
    void load(final ICache<K, V> cache);
}
