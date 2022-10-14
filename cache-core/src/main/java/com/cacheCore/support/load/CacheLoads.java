package com.cacheCore.support.load;

import com.cacheCore.api.ICacheLoad;

/**
 * 加载策略工具类
 *
 * @author sgh
 * @date 2022/10/13 9:25
 */
public final class CacheLoads {

    private CacheLoads() {
    }

    /**
     * 无加载
     *
     * @param <K> key
     * @param <V> value
     * @return instance
     */
    public static <K, V> ICacheLoad<K, V> none() {
        return new CacheLoadNone<>();
    }

    /**
     * DB加载
     *
     * @param path DB文件路径
     * @param <K>  key
     * @param <V>  value
     * @return instance
     */
    public static <K, V> ICacheLoad<K, V> dbJson(final String path) {
        return new CacheLoadDbJson<>(path);
    }

    /**
     * AOF 文件加载模式
     *
     * @param path 文件路径
     * @param <K>  key
     * @param <V>  value
     * @return instance
     */
    public static <K, V> ICacheLoad<K, V> aof(final String path) {
        return new CacheLoadAof<>(path);
    }
}
