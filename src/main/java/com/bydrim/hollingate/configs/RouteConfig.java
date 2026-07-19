package com.bydrim.hollingate.configs;

import com.bydrim.hollingate.requesthandlers.DirectionsHandler;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Optional;

@Configuration
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(GatewayConfig gatewayConfig, DirectionsHandler directionsHandler) {
        if (gatewayConfig.directions().isEmpty()) {
            return req -> Optional.empty();
        }

        RouterFunction<ServerResponse> result = null;
        for (GatewayConfig.Direction dir : gatewayConfig.directions()) {
            final String routeId = dir.type() + " - " + Paths.get(dir.host(), dir.pathPrefix());

            RouterFunction<ServerResponse> router = switch(dir.type()) {
                case STATIC -> GatewayRouterFunctions
                        .route(routeId)
                        .resources((ServerRequest req) -> {
                            try {
                                FileSystem fs = FileSystems.getDefault();
                                String host = Objects.requireNonNullElse(req.headers().firstHeader("host"), "");
                                if (!fs.getPathMatcher("glob:" + dir.host()).matches(Path.of(host))) {
                                    return Optional.empty();
                                }

                                String reqPath= req.path();
                                reqPath = reqPath == null || reqPath.isBlank() ? "/" : reqPath;

                                if (!reqPath.startsWith(dir.pathPrefix())) {
                                    return Optional.empty();
                                }

                                reqPath = reqPath.substring(dir.pathPrefix().length());
                                if (!fs.getPathMatcher("glob:/**.*").matches(Path.of(reqPath))) {
                                    reqPath = Paths.get("/", reqPath, "index.html").toString();
                                }

                                return Optional.of(new FileUrlResource(Paths.get(dir.target(), reqPath).toUri().toURL()));
                            } catch (MalformedURLException e) {
                                return Optional.empty();
                            }
                        })
                        .filter((request, next) -> next.handle(request))
                        .build();
                case PROXY -> {
                    String pathGlob = Paths.get("/", dir.pathPrefix(), "/**").toString();
                    String rewriteRegexp = Paths.get("/", dir.pathPrefix(), "(?<segment>.*)").toString();
                    yield GatewayRouterFunctions
                            .route(routeId)
                            .route(GatewayRequestPredicates.host(dir.host()).and(GatewayRequestPredicates.path(pathGlob)), HandlerFunctions.http())
                            .before(BeforeFilterFunctions.uri(dir.target()))
                            .before(BeforeFilterFunctions.rewritePath(rewriteRegexp, "/${segment}"))
                            .filter((request, next) -> next.handle(request))
                            .build();
                }
                case SELF -> GatewayRouterFunctions
                        .route(routeId)
                        .GET("/directions", GatewayRequestPredicates.host(dir.host()), directionsHandler::viewDirections)
                        .filter((request, next) -> next.handle(request))
                        .build();
            };

            result = result == null ? router : result.and(router);
        }

        return result;
    }
}
