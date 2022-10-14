package com.cacheCore.support.proxy.bs;

import com.cacheCore.annotation.CacheInterceptor;
import com.cacheCore.api.ICache;
import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.api.ICachePersist;
import com.cacheCore.api.ICacheProxyBsContext;
import com.cacheCore.support.interceptor.CacheInterceptorContext;
import com.cacheCore.support.interceptor.CacheInterceptors;
import com.cacheCore.support.persist.CachePersistAof;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 代理引导类
 *
 * @author sgh
 * @date 2022/10/13 8:39
 */
public final class CacheProxyBs {

    private CacheProxyBs() {
    }

    /**
     * 代理上下文
     */
    private ICacheProxyBsContext context;

    /**
     * 默认通用拦截器
     */
    @SuppressWarnings("all")
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.defaultCommonList();

    /**
     * 默认刷新拦截器
     */
    @SuppressWarnings("all")
    private final List<ICacheInterceptor> refreshInterceptors = CacheInterceptors.defaultRefreshList();

    /**
     * 驱除拦截器
     */
    @SuppressWarnings("all")
    private final ICacheInterceptor evictInterceptors = CacheInterceptors.evict();

    /**
     * 持久化拦截器
     */
    @SuppressWarnings("all")
    private final ICacheInterceptor persistInterceptors = CacheInterceptors.aof();

    /**
     * 新建对象实例
     *
     * @return instance
     */
    public static CacheProxyBs newInstance() {
        return new CacheProxyBs();
    }

    public CacheProxyBs context(ICacheProxyBsContext context) {
        this.context = context;
        return this;
    }

    /**
     * 执行
     *
     * @return 方法执行结果
     * @throws InvocationTargetException exception
     * @throws IllegalAccessException    exception
     */
    @SuppressWarnings("all")
    public Object execute() throws InvocationTargetException, IllegalAccessException {
        final long startMills = System.currentTimeMillis();
        final ICache cache = context.target();
        CacheInterceptorContext interceptorContext = CacheInterceptorContext.newInstance()
                .startMills(startMills)
                .method(context.method())
                .params(context.params())
                .cache(cache);

        // 获取刷新注解信息
        CacheInterceptor cacheInterceptor = context.interceptor();
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, true);

        //正常执行
        Object result = context.process();

        final long endMills = System.currentTimeMillis();
        interceptorContext.endMills(endMills).result(result);

        //方法执行完毕
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, false);
        return result;
    }

    /**
     * 拦截器执行
     *
     * @param cacheInterceptor   缓存拦截器
     * @param interceptorContext 拦截器上下文
     * @param cache              缓存
     * @param before             执行时机
     */
    @SuppressWarnings("all")
    private void interceptorHandler(CacheInterceptor cacheInterceptor,
                                    CacheInterceptorContext interceptorContext,
                                    ICache cache, boolean before) {
        if (cacheInterceptor == null) {
            return;
        }
        // 通用
        if (cacheInterceptor.common()) {
            for (ICacheInterceptor interceptor : commonInterceptors) {
                if (before) {
                    interceptor.before(interceptorContext);
                } else {
                    interceptor.after(interceptorContext);
                }
            }
        }

        //刷新
        if (cacheInterceptor.refresh()) {
            for (ICacheInterceptor interceptor : refreshInterceptors) {
                if (before) {
                    interceptor.before(interceptorContext);
                } else {
                    interceptor.after(interceptorContext);
                }
            }
        }
        //AOF追加
        final ICachePersist persist = cache.persist();
        if (cacheInterceptor.aof() && (persist instanceof CachePersistAof)) {
            if (before) {
                persistInterceptors.before(interceptorContext);
            } else {
                persistInterceptors.after(interceptorContext);
            }
        }

        //驱除策略
        if (cacheInterceptor.evict()) {
            if (before) {
                evictInterceptors.before(interceptorContext);
            } else {
                evictInterceptors.after(interceptorContext);
            }
        }
    }
}
