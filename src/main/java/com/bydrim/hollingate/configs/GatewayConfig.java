package com.bydrim.hollingate.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("gateway")
public record GatewayConfig(List<Direction> directions) {
    public record Direction(RouteType type, String host, String pathPrefix, String target) {}
    public enum RouteType { STATIC, PROXY, SELF }
}
