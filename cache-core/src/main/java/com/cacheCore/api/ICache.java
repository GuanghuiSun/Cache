package com.cacheCore.api;

import java.util.List;
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

    /**
     * 加载信息
     *
     * @return 加载信息
     */
    ICacheLoad<K, V> load();

    /**
     * 持久化类
     *
     * @return 持久化类
     */
    ICachePersist<K, V> persist();

    /**
     * 淘汰策略
     *
     * @return 淘汰
     */
    ICacheEvict<K, V> evict();

    /**
     * 删除监听类列表
     *
     * @return 监听器列表
     */
    List<ICacheRemoveListener<K, V>> removeListeners();

    /**
     * 慢日志监听类列表
     * @return 监听器列表
     */
    List<ICacheSlowListener> slowListeners();
}
