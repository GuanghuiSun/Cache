package com.cacheCore.support.proxy.cglib;

import com.cacheCore.api.ICache;
import com.cacheCore.support.proxy.ICacheProxy;
import com.cacheCore.support.proxy.bs.CacheProxyBs;
import com.cacheCore.support.proxy.bs.CacheProxyBsContext;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author sgh
 * @date 2022/10/13 19:07
 */
public class CglibProxy implements MethodInterceptor, ICacheProxy {

    /**
     * 被代理的对象
     */
    private final ICache target;

    public CglibProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        CacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(objects).target(this.target);
        return CacheProxyBs.newInstance().context(context).execute();
    }
}
