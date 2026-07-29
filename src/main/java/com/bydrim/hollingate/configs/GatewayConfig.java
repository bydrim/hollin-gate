package com.bydrim.hollingate.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ConfigurationProperties("gateway")
public record GatewayConfig(List<Direction> directions) {
    public record Direction(RouteType type, List<String> hosts, String pathPrefix, String target) {
        public Direction {
            Objects.requireNonNull(type, "'type' cannot be null. Valid values are 'STATIC', 'PROXY' and 'SELF'.");

            hosts = Objects.requireNonNullElse(hosts, Collections.emptyList());
            if (hosts.stream().anyMatch(h -> h.startsWith("https://") || h.startsWith("http://"))) {
                throw new IllegalArgumentException(
                        "A host should contain only the address as glob and cannot include URL protocols such as 'https://' and 'http://'.");
            }

            pathPrefix = Path.of("/", Objects.requireNonNullElse(pathPrefix, "")).toString();
            if (pathPrefix.equals("/")) {
                pathPrefix = "";
            }

            target = Objects.requireNonNullElse(target, "");
            if (!type.equals(RouteType.SELF) && target.isBlank()) {
                throw new IllegalArgumentException("'target' cannot be blank when 'type' is 'STATIC' or 'PROXY'.");
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{ type: \"").append(type().toString()).append('"');

            sb.append(", hosts: [");
            for (int i = 0; i < hosts().size(); i++) {
                sb.append(" \"").append(hosts().get(i)).append('"');
                if (i == hosts().size() - 1) {
                    sb.append(' ');
                }
                else {
                    sb.append(',');
                }
            }
            sb.append(']');

            sb.append(", pathPrefix: \"").append(pathPrefix()).append('"');

            sb.append(", target: \"").append(target()).append('"');

            sb.append(" }");
            return sb.toString();
        }
    }

    public enum RouteType { STATIC, PROXY, SELF }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n").append("\tdirections: [");
        for (int i = 0; i < directions().size(); i++) {
            sb.append("\n\t\t").append(directions().get(i).toString());
            if (i == directions().size() - 1) {
                sb.append('\n');
            }
            else {
                sb.append(',');
            }
        }
        sb.append("\t]\n");

        sb.append('}');
        return sb.toString();
    }
}
