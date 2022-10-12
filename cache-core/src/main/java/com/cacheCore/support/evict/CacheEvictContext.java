package com.cacheCore.support.evict;


import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEvictContext;

/**
 * 驱除策略上下文实现
 *
 * @author sgh
 * @date 2022/10/11 16:33
 */
public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {
    /**
     * 新加的key
     */
    private K key;

    /**
     * cache实现
     */
    private ICache<K, V> cache;

    /**
     * 最大的大小
     */
    private int size;

    @Override
    public K key() {
        return key;
    }

    public CacheEvictContext<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    @Override
    public ICache<K, V> cache() {
        return cache;
    }

    public CacheEvictContext<K, V> setCache(ICache<K, V> cache) {
        this.cache = cache;
        return this;
    }

    @Override
    public int size() {
        return size;
    }

    public CacheEvictContext<K, V> setSize(int size) {
        this.size = size;
        return this;
    }
}
