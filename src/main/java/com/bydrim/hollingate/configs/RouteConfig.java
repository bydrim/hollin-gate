package com.bydrim.hollingate.configs;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.resource.ResourceHandlerUtils;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.net.MalformedURLException;

@Configuration
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> simpleRoute() throws MalformedURLException {
        return GatewayRouterFunctions.route("router-id")
//                .route(GatewayRequestPredicates.host("localhost:8080"), req -> ServerResponse.ok().body("HOST is " + req.headers().host()))
//                .route(GatewayRequestPredicates.host("localhost:8080"), HandlerFunctions.http())
                .resource(GatewayRequestPredicates.host("localhost:8080"), new FileUrlResource("/home/bydrim/Workspaces/bydrim-com/.output/public"))
//                .before(BeforeFilterFunctions.uri("http://localhost:4040"))
                .before(req -> {
                    // req.param("tid");
                    return req;
                })
                .build();
    }
}
