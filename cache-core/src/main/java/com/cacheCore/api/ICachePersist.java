package com.cacheCore.api;

import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化接口
 *
 * @author sgh
 * @date 2022/10/12 9:41
 */
public interface ICachePersist<K, V> {

    /**
     * 持久化缓存信息
     *
     * @param cache 缓存
     */
    void persist(ICache<K, V> cache);

    /**
     * 延迟时间
     *
     * @return 延迟
     */
    long delay();

    /**
     * 时间间隔
     *
     * @return 间隔
     */
    long period();

    /**
     * 时间单位
     *
     * @return 时间单位
     */
    TimeUnit timeUnit();
}
