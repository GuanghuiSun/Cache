package com.cacheCore.support.interceptor.refresh;

import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.api.ICacheInterceptorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 刷新缓存拦截器
 *
 * @author sgh
 * @date 2022/10/13 14:42
 */
@Slf4j
public class CacheRefreshInterceptor<K, V> implements ICacheInterceptor<K, V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Refresh start...");
        final ICache<K, V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
    }
}
