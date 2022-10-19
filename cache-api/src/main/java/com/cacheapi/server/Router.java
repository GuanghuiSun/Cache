package com.cacheapi.server;

import com.cacheCore.api.ICache;
import com.cacheapi.consistentHash.ConsistentHashingWithoutVirtualNode;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 路由器
 * 根据缓存的 key 路由到正确的节点上去
 *
 * @author sgh
 * @date 2022/10/18 15:41
 */
@Slf4j
public class Router {

    /**
     * 虚拟节点与真实节点的倍数关系
     */
    private final int replicas;

    /**
     * 地址——结点 映射
     */
    private Map<String, ServerNode<String, String>> addrMap = new HashMap<>();

    /**
     * 哈希环 一致性哈希关键
     */
    private ConsistentHashingWithoutVirtualNode hashCircle;

    public Router(int replicas) {
        this.replicas = replicas;
        this.hashCircle = new ConsistentHashingWithoutVirtualNode(replicas);
    }

    /**
     * 添加节点
     *
     * @param addr 地址
     */
    public ServerNode<String, String> addServerNode(String addr) {
        ServerNode<String, String> node = new ServerNode<>(addr);
        hashCircle.add(addr);
        addrMap.put(addr, node);
        log.debug("Add ServerNode, address:{}", addr);
        return node;
    }

    /**
     * 根据 key 获取目标节点
     *
     * @param key key
     * @return 目标节点
     */
    public ServerNode<String, String> getTargetServerNode(String key) {
        String addr = hashCircle.get(key);
        log.debug("Key : {} get target server node : {}", key ,addr);
        return addrMap.get(addr);
    }

    /**
     * 根据地址获取节点
     *
     * @param addr 地址
     * @return 节点
     */
    public ServerNode<String, String> getServerNode(String addr) {
        return addrMap.get(addr);
    }

    /**
     * 删除指定节点
     *
     * @param addr 地址
     */
    public void deleteServerNode(String addr) {
        //删除哈希环中节点
        hashCircle.delete(addr);

        ServerNode<String, String> node = addrMap.get(addr);

        //复制缓存给其他节点
        ICache<String, String> cache = node.cache();
        for (String key : cache.keySet()) {
            ServerNode<String, String> targetServerNode = this.getTargetServerNode(key);
            targetServerNode.cache().put(key, cache.get(key));
        }

        addrMap.remove(addr);
    }
}
