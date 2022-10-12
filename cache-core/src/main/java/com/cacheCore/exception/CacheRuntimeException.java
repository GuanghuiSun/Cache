package com.cacheCore.exception;

/**
 * @author sgh
 * @date 2022/10/11 9:56
 */
public class CacheRuntimeException extends RuntimeException{

    public CacheRuntimeException() {}

    public CacheRuntimeException(String message) {
        super(message);
    }

    public CacheRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheRuntimeException(Throwable cause) {
        super(cause);
    }
}
