package com.cacheCore.support.interceptor.common;

import com.cacheCore.api.ICacheInterceptor;
import com.cacheCore.api.ICacheInterceptorContext;
import com.cacheCore.api.ICacheSlowListener;
import com.cacheCore.support.listener.slow.CacheSlowListenerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 耗时统计
 *
 * @author sgh
 * @date 2022/10/13 14:50
 */
@Slf4j
public class CacheCostInterceptor<K, V> implements ICacheInterceptor<K, V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        long costMills = context.startMills() - context.endMills();
        final String methodName = context.method().getName();
        log.debug("Cost end, method: {} cost: {}", methodName, costMills);

        //添加慢日志操作
        List<ICacheSlowListener> slowListeners = context.cache().slowListeners();
        if (!slowListeners.isEmpty()) {
            CacheSlowListenerContext listenerContext = CacheSlowListenerContext.newInstance()
                    .startTimeMills(context.startMills())
                    .endTimeMills(context.endMills())
                    .params(context.params())
                    .methodName(methodName)
                    .result(context.result());
            for (ICacheSlowListener slowListener : slowListeners) {
                long threshold = slowListener.slowThreshold();
                if (costMills >= threshold)
                    //打印慢操作日志
                    slowListener.listen(listenerContext);
            }
        }



    }
}
