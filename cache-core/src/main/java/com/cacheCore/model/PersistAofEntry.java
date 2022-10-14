package com.cacheCore.model;

import java.util.Arrays;

/**
 * AOF持久化明细
 *
 * @author sgh
 * @date 2022/10/13 20:27
 */
public class PersistAofEntry {

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数列表
     */
    private Object[] params;

    /**
     * 实例化
     * @return instance
     */
    public static PersistAofEntry newInstance() {
        return new PersistAofEntry();
    }

    @Override
    public String toString() {
        return "PersistAofEntry{" +
                "methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
