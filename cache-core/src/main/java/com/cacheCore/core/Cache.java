package com.cacheCore.core;


import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheEntry;
import com.cacheCore.api.ICacheEvict;
import com.cacheCore.api.ICacheExpire;
import com.cacheCore.exception.CacheRuntimeException;
import com.cacheCore.support.evict.CacheEvictContext;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author sgh
 * @date 2022/10/11 16:09
 */
public class Cache<K, V> implements ICache<K, V> {

    /**
     * map信息
     */
    private Map<K, V> map;

    /**
     * 大小限制
     */
    private int sizeLimit;

    /**
     * 驱除策略
     */
    private ICacheEvict<K, V> evict;

    /**
     * 过期策略
     */
    private ICacheExpire<K, V> expire;

    /**
     * 设置map的实现
     *
     * @param map map实现类对象
     * @return this
     */
    public Cache<K, V> map(Map<K, V> map) {
        this.map = map;
        return this;
    }

    /**
     * 设置大小限制
     *
     * @param sizeLimit 大小限制
     * @return this
     */
    public Cache<K, V> sizeLimit(int sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    /**
     * 设置驱除策略
     *
     * @param cacheEvict 驱除策略
     * @return this
     */
    public Cache<K, V> evict(ICacheEvict<K, V> cacheEvict) {
        this.evict = cacheEvict;
        return this;
    }


    @Override
    public ICache<K, V> expire(K key, long timeInMills) {
        return null;
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeInMills) {
        return null;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return this.expire;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        //1.尝试驱除
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.setKey(key).setSize(sizeLimit).setCache(this);

        ICacheEntry<K, V> evictEntry = this.evict.evict(context);

        if (isSizeLimit()) {
            throw new CacheRuntimeException("当前队列已满，数据添加失败!");
        }
        return map.put(key, value);
    }

    /**
     * 是否已经达到大小上限
     *
     * @return result
     */
    private boolean isSizeLimit() {
        return this.size() >= this.sizeLimit;
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
