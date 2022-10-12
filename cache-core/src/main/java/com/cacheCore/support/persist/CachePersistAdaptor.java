package com.cacheCore.support.persist;


import com.cacheCore.api.ICache;
import com.cacheCore.api.ICachePersist;

import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化策略 —— 适配器模式
 *
 * @author sgh
 * @date 2022/10/12 9:45
 */
public class CachePersistAdaptor<K, V> implements ICachePersist<K, V> {
    @Override
    public void persist(ICache<K, V> cache) {

    }

    @Override
    public long delay() {
        return this.period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
