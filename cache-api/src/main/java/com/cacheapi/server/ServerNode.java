package com.cacheapi.server;

import com.cacheCore.api.ICache;
import com.cacheCore.bs.CacheBs;
import com.cacheCore.core.Cache;

import java.lang.reflect.InvocationTargetException;

/**
 * 服务节点
 *
 * @author sgh
 * @date 2022/10/18 10:17
 */
public class ServerNode<K, V> {
    /**
     * 缓存
     */
    private Cache<K, V> cache;

    /**
     * 地址
     */
    private final String addr;

    public ServerNode(String addr) {
        this.addr = addr;
        try {
            this.cache = (Cache<K, V>) CacheBs.newInstance().build();

        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Cache<K, V> cache() {
        return this.cache;
    }

    public ServerNode<K, V> cache(Cache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    public String addr() {
        return this.addr;
    }

    /**
     * 缓存 kv
     *
     * @param key   键
     * @param value 值
     */
    public void put(K key, V value) {
        this.cache.put(key, value);
    }

    /**
     * 带有过期时间的缓存
     *
     * @param key         键
     * @param value       值
     * @param expireMills 过期时间
     */
    public void put(K key, V value, long expireMills) {
        this.put(key, value);
        this.cache.expire(key, expireMills);
    }

    /**
     * 获取键对应的值
     *
     * @param key 键
     * @return value
     */
    public V get(K key) {
        return this.cache.get(key);
    }

}
