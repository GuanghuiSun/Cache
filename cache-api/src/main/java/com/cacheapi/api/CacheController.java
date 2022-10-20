package com.cacheapi.api;

import com.cacheapi.model.CacheEntity;
import com.cacheapi.server.ServerConfig;
import com.cacheapi.server.ServerNode;
import com.cacheapi.singleFlight.CallManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sun.util.resources.cldr.ar.CalendarData_ar_LB;

import javax.annotation.Resource;

/**
 * 接收http请求 进行缓存操作
 *
 * @author sgh
 * @date 2022/10/18 16:41
 */
@RestController
@RequestMapping("/cache")
@Slf4j
public class CacheController {

    @Resource
    private ServerConfig serverConfig;

    /**
     * 获取缓存
     *
     * @param key key
     * @return value
     */
    @GetMapping("/{key}")
    public String getWithSingleFlight(@PathVariable String key) {
        CallManager callManager = new CallManager();
        return callManager.run(key, this::get);
    }

    private String get(String key) {
        ServerNode<String, String> node = serverConfig.router().getTargetServerNode(key);
        log.debug("Get key:{} value from server node: {}", key, node.addr());
        return node.cache().get(key);
    }

    /**
     * 添加缓存
     *
     * @param cache 缓存实体
     * @return success
     */
    @PostMapping()
    public Boolean put(@RequestBody CacheEntity cache) {
        ServerNode<String, String> targetServerNode = serverConfig.router().getTargetServerNode(cache.getKey());
        targetServerNode.cache().put(cache.getKey(), cache.getValue());
        log.debug("key: {}, value: {} has been put into server node: {}", cache.getKey(), cache.getValue(), targetServerNode.addr());
        return Boolean.TRUE;
    }

    /**
     * 删除缓存
     *
     * @param key key
     * @return success
     */
    @DeleteMapping("/{key}")
    public Boolean delete(@PathVariable String key) {
        ServerNode<String, String> node = serverConfig.router().getTargetServerNode(key);
        node.cache().remove(key);
        log.debug("Remove key:{} value from server node: {}", key, node.addr());
        return Boolean.TRUE;
    }
}
