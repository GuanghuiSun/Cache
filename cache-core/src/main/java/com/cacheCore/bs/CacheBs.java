package com.cacheCore.bs;

import com.cacheCore.api.*;
import com.cacheCore.constant.PersistAndLoadType;
import com.cacheCore.core.Cache;
import com.cacheCore.support.evict.CacheEvicts;
import com.cacheCore.support.listener.remove.CacheRemoveListeners;
import com.cacheCore.support.listener.slow.CacheSlowListeners;
import com.cacheCore.support.load.CacheLoads;
import com.cacheCore.support.persist.CachePersists;
import com.cacheCore.support.proxy.CacheProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存引导类
 *
 * @author sgh
 * @date 2022/10/13 9:04
 */
public class CacheBs<K, V> {

    private CacheBs() {
    }

    /**
     * 创建实例对象
     *
     * @param <K> key
     * @param <V> value
     * @return instance
     */
    public static <K, V> CacheBs<K, V> newInstance() {
        return new CacheBs<>();
    }

    /**
     * map实现
     */
    private Map<K, V> map = new HashMap<>();

    /**
     * 大小限制
     */
    private int size = Integer.MAX_VALUE;

    /**
     * 驱除策略
     */
    private ICacheEvict<K, V> evict = CacheEvicts.fifo();

    /**
     * 持久化策略
     */
    private ICachePersist<K, V> persist = CachePersists.none();

    /**
     * 加载策略
     */
    private ICacheLoad<K, V> load = CacheLoads.none();

    /**
     * 删除监听器集合
     */
    private final List<ICacheRemoveListener<K, V>> removeListeners = CacheRemoveListeners.defaults();

    /**
     * 慢操作监听集合
     */
    private final List<ICacheSlowListener> slowListeners = CacheSlowListeners.none();

    /**
     * 配置map实现
     *
     * @param map map实现
     * @return this
     */
    public CacheBs<K, V> map(Map<K, V> map) {
        if (map != null)
            this.map = map;
        return this;
    }

    /**
     * 配置大小限制
     *
     * @param size 大小限制
     * @return this
     */
    public CacheBs<K, V> size(int size) {
        if (size > 0)
            this.size = size;
        return this;
    }

    /**
     * 配置驱除策略
     *
     * @param evict 驱除策略
     * @return this
     */
    public CacheBs<K, V> evict(ICacheEvict<K, V> evict) {
        if (evict != null)
            this.evict = evict;
        return this;
    }

    /**
     * 配置持久化加载策略
     *
     * @param type 类型
     * @param path 路径
     * @return this
     */
    public CacheBs<K, V> persistAndLoad(PersistAndLoadType type, String path) {
        if (type == PersistAndLoadType.DbJson) {
            this.persist = CachePersists.dbJson(path);
            this.load = CacheLoads.dbJson(path);
        } else if (type == PersistAndLoadType.AOF) {

        }
        return this;
    }

    /**
     * 配置持久化策略
     *
     * @param persist 持久化策略
     * @return this
     */
    public CacheBs<K, V> persist(ICachePersist<K, V> persist) {
        if (persist != null)
            this.persist = persist;
        return this;
    }

    /**
     * 配置加载策略
     *
     * @param load 加载策略
     * @return this
     */
    public CacheBs<K, V> load(ICacheLoad<K, V> load) {
        if (load != null)
            this.load = load;
        return this;
    }

    /**
     * 添加删除监听器
     *
     * @param removeListener 自定义删除监听器
     * @return this
     */
    public CacheBs<K, V> addRemoveListener(ICacheRemoveListener<K, V> removeListener) {
        this.removeListeners.add(removeListener);
        return this;
    }

    /**
     * 添加慢操作监听器
     *
     * @param slowListener 自定义慢操作监听器
     * @return this
     */
    public CacheBs<K, V> addSlowListener(ICacheSlowListener slowListener) {
        this.slowListeners.add(slowListener);
        return this;
    }

    /**
     * 构建缓存信息
     *
     * @return 缓存信息
     */
    public ICache<K, V> build() {
        Cache<K, V> cache = new Cache<>();
        cache.map(map);
        cache.evict(evict);
        cache.sizeLimit(size);
        cache.removeListeners(removeListeners);
        cache.load(load);
        cache.persist(persist);
        cache.slowListeners(slowListeners);

        //初始化
        cache.init();
        return CacheProxy.getProxy(cache);
    }
}
