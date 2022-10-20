package com.cacheapi.singleFlight;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 *
 * single flight 实现主类 管理不同key的请求
 *
 * @author sgh
 * @date 2022/10/20 10:28
 */
@Slf4j
public class CallManager {
    private final Lock lock = new ReentrantLock();
    private Map<String, Call> callMap;

    /**
     * 主实现函数
     * @param key 键
     * @param func 函数
     * @return 值
     */
    public String run(String key, Function<String, String> func) {
        this.lock.lock();
        if (this.callMap == null)
            this.callMap = new HashMap<>();
        Call call = callMap.get(key);
        //当前请求已经有人在执行了
        if (call != null) {
            this.lock.unlock();
            call.await();
            log.debug("Get await value...");
            return call.getVal();
        }
        call = new Call();
        call.lock();
        this.callMap.put(key, call);
        this.lock.unlock();

        call.setVal(func.apply(key));
        call.done();
        this.lock.lock();
        this.callMap.remove(key);
        this.lock.unlock();
        log.debug("Get operation value...");
        return call.getVal();
    }
}
