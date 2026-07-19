package com.bydrim.hollingate.requesthandlers;

import com.bydrim.hollingate.configs.GatewayConfig;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class DirectionsHandler {
    private final TemplateEngine templateEngine;
    private final GatewayConfig gatewayConfig;

    public DirectionsHandler(TemplateEngine templateEngine,  GatewayConfig gatewayConfig) {
        this.templateEngine = templateEngine;
        this.gatewayConfig = gatewayConfig;
    }

    public ServerResponse viewDirections(ServerRequest req) {
        TemplateOutput output = new StringOutput();
        templateEngine.render("directions.jte", gatewayConfig.directions(), output);
        return ServerResponse.ok().header("content-type", "text/html").body(output.toString());
    }
}
