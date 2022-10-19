package com.cacheapi.server;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sgh
 * @date 2022/10/18 19:24
 */
@Configuration
@ConfigurationProperties(prefix = "router")
public class ServerConfig {
    private List<String> serverAddress = new ArrayList<>();
    private int replicas;

    private Router router = null;

    public Router router() {
        if (router == null) {
            router = new Router(replicas);
            for (String address : serverAddress) {
                router.addServerNode(address);
            }
        }
        return router;
    }

    public List<String> getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(List<String> serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }
}
