package com.bydrim.hollingate.configs;

import com.bydrim.hollingate.requesthandlers.DirectionsHandler;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
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
            RouterFunction<ServerResponse> router = switch(dir.type()) {
                case STATIC -> GatewayRouterFunctions
                        .route(dir.toString())
                        .resources((ServerRequest req) -> {
                            try {
                                if (!hostPredicate(dir.hosts()).test(req)) {
                                    return Optional.empty();
                                }

                                String pathGlob = Path.of(dir.pathPrefix(), "/**").toString();
                                if (!GatewayRequestPredicates.path(pathGlob).test(req)) {
                                    return Optional.empty();
                                }

                                String rewriteRegexp = dir.pathPrefix() + "(?<segment>.*)";
                                req = BeforeFilterFunctions.rewritePath(rewriteRegexp, "${segment}").apply(req);

                                File requestedFile = Path.of(dir.target(), req.uri().getPath()).toFile();
                                if (!requestedFile.exists()) {
                                    return Optional.empty();
                                }
                                else if (requestedFile.isDirectory()) {
                                    // req = BeforeFilterFunctions.rewritePath("(?<segment>.*[^/])", "${segment}/index.html").apply(req);
                                    return Optional.of(new FileUrlResource(requestedFile.toPath().resolve("index.html").toUri().toURL()));
                                }
                                else {
                                    return Optional.of(new FileUrlResource(requestedFile.toURI().toURL()));
                                }
                            } catch (MalformedURLException e) {
                                return Optional.empty();
                            }
                        })
                        .filter((request, next) -> next.handle(request))
                        .build();
                case PROXY -> {
                    String pathGlob = Path.of(dir.pathPrefix(), "/**").toString();
                    String rewriteRegexp = dir.pathPrefix() + "(?<segment>.*)";
                    yield GatewayRouterFunctions
                            .route(dir.toString())
                            .route(hostPredicate(dir.hosts()).and(GatewayRequestPredicates.path(pathGlob)), HandlerFunctions.http())
                            .before(BeforeFilterFunctions.uri(dir.target()))
                            .before(BeforeFilterFunctions.rewritePath(rewriteRegexp, "${segment}"))
                            .filter((request, next) -> next.handle(request))
                            .build();
                }
                case SELF -> GatewayRouterFunctions
                        .route(dir.toString())
                        .path(dir.pathPrefix(), builder -> builder
                                .GET("/directions", hostPredicate(dir.hosts()), directionsHandler::viewDirections))
                        .filter((request, next) -> next.handle(request))
                        .build();
            };

            result = result == null ? router : result.and(router);
        }

        return result;
    }

    /**
     * This implementation exists because 'RequestParameters.host' method tests using org.springframework.web.util.pattern.PathPattern
     * and in that style there is no way to tell 'match all paths'. In that style, one has to include a path separator at least
     * and host names like 'localhost:8080' can never be expressed in 'match all paths' string.
     * @param host
     * @return
     */
    private RequestPredicate hostPredicate(List<String> hosts) {
        if (null == hosts || hosts.isEmpty() || hosts.stream().anyMatch(h -> h.isBlank() || h.equals("**"))) {
            return req -> true;
        }
        return GatewayRequestPredicates.host(hosts.toArray(String[]::new));
    }
}
