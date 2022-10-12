package com.cacheCore.api;

import java.util.Collection;

/**
 * 缓存过期策略
 *
 * @author sgh
 * @date 2022/10/11 16:13
 */
public interface ICacheExpire<K, V> {

    /**
     * 指定过期时间
     *
     * @param key      key
     * @param expireAt 过期时间戳
     */
    void expire(final K key, long expireAt);

    /**
     * 惰性删除需要处理的key
     *
     * @param keyList keys
     */
    void refreshExpire(final Collection<K> keyList);

    /**
     * key的过期时间
     * 不存在，返回null
     *
     * @param key key
     * @return 过期时间
     */
    Long expireTime(final K key);

}
