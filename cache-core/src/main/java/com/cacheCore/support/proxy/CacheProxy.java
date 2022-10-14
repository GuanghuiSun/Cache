package com.cacheCore.support.proxy;

import com.cacheCore.api.ICache;
import com.cacheCore.support.proxy.cglib.CglibProxy;
import com.cacheCore.support.proxy.dynamic.DynamicProxy;
import com.cacheCore.support.proxy.none.NoneProxy;

import java.lang.reflect.Proxy;

/**
 * 代理
 *
 * @author sgh
 * @date 2022/10/13 10:44
 */
public class CacheProxy {

    private CacheProxy() {}

    public static <K, V> ICache<K, V> getProxy(final ICache<K, V> cache) {
        if (cache == null) {
            return (ICache<K, V>) new NoneProxy(cache).proxy();
        }

        final Class clazz = cache.getClass();

        if (clazz.isInterface() || Proxy.isProxyClass(clazz)) {
            return (ICache<K, V>) new DynamicProxy(cache).proxy();
        }
        return (ICache<K, V>) new CglibProxy(cache).proxy();
    }
}
