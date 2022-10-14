package com.cacheCore.support.persist;

import com.cacheCore.api.ICachePersist;

/**
 * 持久化策略工具类
 *
 * @author sgh
 * @date 2022/10/13 9:44
 */
public class CachePersists {

    private CachePersists() {
    }

    /**
     * 无持久化策略
     *
     * @param <K> key
     * @param <V> value
     * @return instance
     */
    public static <K, V> ICachePersist<K, V> none() {
        return new CachePersistNone<>();
    }

    /**
     * DB持久化策略
     *
     * @param path DB文件路径
     * @param <K>  key
     * @param <V>  value
     * @return instance
     */
    public static <K, V> ICachePersist<K, V> dbJson(final String path) {
        return new CachePersistDbJson<>(path);
    }

    /**
     * AOF 持久化策略
     *
     * @param path 文件路径
     * @param <K>  key
     * @param <V>  value
     * @return instance
     */
    public static <K, V> ICachePersist<K, V> aof(final String path) {
        return new CachePersistAof<>(path);
    }
}
