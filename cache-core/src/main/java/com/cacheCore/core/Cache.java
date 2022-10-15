package com.cacheCore.core;


import com.cacheCore.annotation.CacheInterceptor;
import com.cacheCore.api.*;
import com.cacheCore.constant.CacheRemoveType;
import com.cacheCore.exception.CacheRuntimeException;
import com.cacheCore.support.evict.CacheEvictContext;
import com.cacheCore.support.expire.CacheExpire;
import com.cacheCore.support.listener.remove.CacheRemoveListenerContext;
import com.cacheCore.support.persist.InnerCachePersist;
import com.cacheCore.support.proxy.CacheProxy;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
     * 持久化策略
     */
    private ICachePersist<K, V> persist;

    /**
     * 加载策略
     */
    private ICacheLoad<K, V> load;

    /**
     * 删除监听器集合
     */
    private List<ICacheRemoveListener<K, V>> removeListeners;

    /**
     * 慢日志监听类
     */
    private List<ICacheSlowListener> slowListeners;

    /**
     * 初始化
     */
    public void init() throws InvocationTargetException, IllegalAccessException {
        this.expire = new CacheExpire<>(this);
        this.load.load(this);

        //初始持久化
        if (this.persist != null) {
            new InnerCachePersist<>(this, persist);
        }
    }

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

    public Cache<K, V> persist(ICachePersist<K, V> persist) {
        this.persist = persist;
        return this;
    }

    public Cache<K, V> load(ICacheLoad<K, V> load) {
        this.load = load;
        return this;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return slowListeners;
    }

    public Cache<K, V> slowListeners(List<ICacheSlowListener> slowListeners) {
        this.slowListeners = slowListeners;
        return this;
    }

    /**
     * 设置过期时间
     *
     * @param key         key
     * @param timeInMills 存活时间
     * @return this
     */
    @Override
    @CacheInterceptor
    public ICache<K, V> expire(K key, long timeInMills) {
        long expireTime = System.currentTimeMillis() + timeInMills;

        //代理调用
        Cache<K, V> cacheProxy = (Cache<K, V>) CacheProxy.getProxy(this);
        return cacheProxy.expireAt(key, expireTime);
    }

    /**
     * 指定过期时间
     *
     * @param key         key
     * @param timeInMills 毫秒过期时间戳
     * @return this
     */
    @Override
    @CacheInterceptor(aof = true)
    public ICache<K, V> expireAt(K key, long timeInMills) {
        this.expire.expire(key, timeInMills);
        return this;
    }

    @Override
    @CacheInterceptor
    public ICacheExpire<K, V> expire() {
        return this.expire;
    }

    @Override
    public ICacheLoad<K, V> load() {
        return this.load;
    }

    @Override
    public ICachePersist<K, V> persist() {
        return this.persist;
    }

    @Override
    public ICacheEvict<K, V> evict() {
        return this.evict;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return this.removeListeners;
    }

    /**
     * 添加监听器
     *
     * @param removeListeners 删除监听器
     * @return this
     */
    public Cache<K, V> removeListeners(List<ICacheRemoveListener<K, V>> removeListeners) {
        this.removeListeners = removeListeners;
        return this;
    }

    @Override
    @CacheInterceptor(refresh = true)
    public int size() {
        return map.size();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    @CacheInterceptor(refresh = true)
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    @CacheInterceptor(evict = true)
    public V get(Object key) {
        K genericKey = (K) key;
        this.expire.refreshExpire(Collections.singletonList(genericKey));
        return map.get(key);
    }

    @Override
    @CacheInterceptor(aof = true, evict = true)
    public V put(K key, V value) {
        //1.尝试驱除
        CacheEvictContext<K, V> context = new CacheEvictContext<>();
        context.setKey(key).setSize(sizeLimit).setCache(this);

        ICacheEntry<K, V> evictEntry = this.evict.evict(context);

        //添加拦截器调用
        if (evictEntry != null) {
            //淘汰监听器
            CacheRemoveListenerContext<K, V> removeListenerContext = CacheRemoveListenerContext.<K, V>newInstance()
                    .key(evictEntry.key()).value(evictEntry.value())
                    .type(CacheRemoveType.EXPIRE.code());
            for (ICacheRemoveListener<K, V> removeListener : context.cache().removeListeners()) {
                removeListener.listen(removeListenerContext);
            }
        }

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
    @CacheInterceptor(aof = true, evict = true)
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    @CacheInterceptor(aof = true)
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    @CacheInterceptor(aof = true, evict = true)
    public void clear() {
        map.clear();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Collection<V> values() {
        return map.values();
    }

    @Override
    @CacheInterceptor(refresh = true)
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
