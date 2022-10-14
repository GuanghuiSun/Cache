package com.cacheCore.support.interceptor.evict;

import com.cacheCore.api.ICacheEvict;
import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.api.ICacheInterceptorContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 驱除拦截器
 *
 * @author sgh
 * @date 2022/10/13 14:45
 */
@Slf4j
public class CacheEvictInterceptor<K, V> implements ICacheInterceptor<K, V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K, V> context) {
        final ICacheEvict<K, V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if ("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else {
            evict.updateKey(key);
        }
    }
}
