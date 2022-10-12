package com.cacheCore.model;


import com.cacheCore.api.ICacheEntry;

/**
 * 缓存数据实体 key-value
 *
 * @author sgh
 * @date 2022/10/11 18:32
 */
public class CacheEntry<K, V> implements ICacheEntry<K, V> {

    /**
     * key
     */
    private final K key;

    /**
     * value
     */
    private final V value;

    public static <K, V> CacheEntry<K, V> of(final K key, final V value) {
        return new CacheEntry<>(key, value);
    }

    public CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public String toString() {
        return "CacheEntry{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public V value() {
        return value;
    }
}
