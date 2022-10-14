package com.cacheCore.support.listener.slow;

import com.alibaba.fastjson.JSON;
import com.cacheCore.api.ICacheSlowListener;
import com.cacheCore.api.ICacheSlowListenerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sgh
 * @date 2022/10/13 19:41
 */
@Slf4j
public class CacheSlowListener implements ICacheSlowListener {

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowThreshold() {
        return 1000L;
    }
}
