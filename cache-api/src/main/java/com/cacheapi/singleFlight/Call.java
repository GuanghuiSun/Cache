package com.cacheapi.singleFlight;

import java.util.concurrent.CountDownLatch;

/**
 * single flight Java 实现
 * 多个相同的请求 只执行一次查询 返回相同结果
 *
 * 表示正在执行或者已结束的请求
 *
 * @author sgh
 * @date 2022/10/20 10:24
 */
public class Call {
    private String val;
    private CountDownLatch cdl;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void await() {
        try {
            this.cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void lock() {
        this.cdl = new CountDownLatch(1);
    }

    public void done() {
        this.cdl.countDown();
    }
}
