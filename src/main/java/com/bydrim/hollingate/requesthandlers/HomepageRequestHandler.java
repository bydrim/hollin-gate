package com.bydrim.hollingate.requesthandlers;

import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class HomepageRequestHandler {
    private final TemplateEngine templateEngine;

    public HomepageRequestHandler(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public ServerResponse viewHomepage(ServerRequest req) {
        TemplateOutput output = new StringOutput();
        templateEngine.render("index.jte", null, output);
        return ServerResponse.ok().header("content-type", "text/html").body(output.toString());
    }
}
