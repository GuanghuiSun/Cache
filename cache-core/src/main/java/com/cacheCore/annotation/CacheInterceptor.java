package com.cacheCore.annotation;

import java.lang.annotation.*;

/**
 * 缓存拦截器注解
 *
 * @author sgh
 * @date 2022/10/13 15:29
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheInterceptor {

    /**
     * 通用拦截器
     * 1.耗时统计
     * 2.慢日志统计
     *
     * @return 默认开启
     */
    boolean common() default true;

    /**
     * 是否启用刷新拦截器
     *
     * @return 默认关闭
     */
    boolean refresh() default false;

    /**
     * 操作是否需要 append to file 默认为false
     * 主要针对 cache 变更操作 不包括查询
     * 增删过期操作
     * @return false
     */
    boolean aof() default false;

    /**
     * 是否执行驱除更新
     *
     * 主要用于LRU/LFU
     * @return false
     */
    boolean evict() default false;
}
