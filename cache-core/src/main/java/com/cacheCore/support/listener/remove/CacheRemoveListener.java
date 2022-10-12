package com.cacheCore.support.listener.remove;

import com.cacheCore.api.ICacheRemoveListener;
import com.cacheCore.api.ICacheRemoveListenerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 删除监听器实现
 *
 * @author sgh
 * @date 2022/10/12 21:52
 */
@Slf4j
public class CacheRemoveListener<K, V> implements ICacheRemoveListener<K, V> {

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type:{}", context.key(), context.value(), context.type());
    }
}
