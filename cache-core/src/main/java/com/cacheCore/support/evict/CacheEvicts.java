package com.cacheCore.support.evict;

import com.cacheCore.api.ICacheEvict;

/**
 * 丢弃策略
 *
 * @author sgh
 * @date 2022/10/13 9:08
 */
public final class CacheEvicts {

    private CacheEvicts() {
    }

    /**
     * 无策略
     *
     * @param <K> key
     * @param <V> value
     * @return none evict
     */
    public static <K, V> ICacheEvict<K, V> none() {
        return new CacheEvictNone<>();
    }

    /**
     * 先进先出
     *
     * @param <K> key
     * @param <V> value
     * @return FIFO evict
     */
    public static <K, V> ICacheEvict<K, V> fifo() {
        return new CacheEvictFifo<>();
    }

}
