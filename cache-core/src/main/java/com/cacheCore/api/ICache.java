package com.cacheCore.api;

import java.util.Map;

/**
 * 缓存接口
 *
 * @author sgh
 * @date 2022/10/10 17:56
 */
public interface ICache<K, V> extends Map<K, V> {

    /**
     * 设置过期时间
     *
     * @param key         key
     * @param timeInMills 存活时间
     * @return this
     */
    ICache<K, V> expire(final K key, final long timeInMills);

    /**
     * 设置何时过期
     *
     * @param key         key
     * @param timeInMills 过期时间戳
     * @return this
     */
    ICache<K, V> expireAt(final K key, final long timeInMills);

    /**
     * 获取缓存的过期处理实现类
     *
     * @return 处理类实现
     */
    ICacheExpire<K, V> expire();
}
