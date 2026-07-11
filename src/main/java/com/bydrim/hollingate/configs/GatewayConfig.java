package com.bydrim.hollingate.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("gateway")
public class GatewayConfig {
    private final List<DirectionItem> directions;

    public List<DirectionItem> getDirections() {
        return directions;
    }

    public GatewayConfig(List<DirectionItem> directions) {
        this.directions = directions;
    }

    public static class DirectionItem {
        private final String from;
        private final String to;

        public DirectionItem(String from, String to) {
            this.from = from;
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }
    }
}

