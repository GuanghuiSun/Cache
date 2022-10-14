package com.cacheCore.support.interceptor;

import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.support.interceptor.aof.CacheAofInterceptor;
import com.cacheCore.support.interceptor.common.CacheCostInterceptor;
import com.cacheCore.support.interceptor.evict.CacheEvictInterceptor;
import com.cacheCore.support.interceptor.refresh.CacheRefreshInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存拦截工具类
 *
 * @author sgh
 * @date 2022/10/13 14:57
 */
public final class CacheInterceptors {

    /**
     * 默认通用
     *
     * @return 慢日志监听
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultCommonList() {
        List<ICacheInterceptor> res = new ArrayList<>();
        res.add(new CacheCostInterceptor());
        return res;
    }

    /**
     * 默认刷新
     *
     * @return 刷新拦截器
     */
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defaultRefreshList() {
        List<ICacheInterceptor> res = new ArrayList<>();
        res.add(new CacheRefreshInterceptor());
        return res;
    }

    /**
     * 驱除策略拦截器
     *
     * @return 驱除策略拦截器
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor evict() {
        return new CacheEvictInterceptor();
    }

    /**
     * AOF 模式
     *
     * @return AOF拦截器
     */
    @SuppressWarnings("all")
    public static ICacheInterceptor aof() {
        return new CacheAofInterceptor();
    }

}
