package com.bydrim.hollingate.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@ConfigurationProperties("gateway")
public record GatewayConfig(List<Direction> directions) {
    public record Direction(RouteType type, String host, String pathPrefix, String target) {
        public Direction {
            Objects.requireNonNull(type, "'type' cannot be null. Valid values are 'STATIC', 'PROXY' and 'SELF'.");

            host = Objects.requireNonNullElse(host, "");
            if (host.startsWith("https://") || host.startsWith("http://")) {
                throw new IllegalArgumentException(
                        "'host' should contain only the address as glob and cannot include URL protocols such as 'https://' and 'http://'.");
            }

            pathPrefix = Path.of("/", Objects.requireNonNullElse(pathPrefix, "")).toString();

            target = Objects.requireNonNullElse(target, "");
            if (!type.equals(RouteType.SELF) && target.isBlank()) {
                throw new IllegalArgumentException("'target' cannot be blank when 'type' is 'STATIC' or 'PROXY'.");
            }
        }
    }
    public enum RouteType { STATIC, PROXY, SELF }
}
