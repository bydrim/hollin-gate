package com.bydrim.hollingate.requesthandlers;

import com.bydrim.hollingate.entities.Tracker;
import com.bydrim.hollingate.services.TrackerService;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;

@Component
public class TrackerRequestHandler {
    private final TemplateEngine templateEngine;
    private final TrackerService trackerService;

    public TrackerRequestHandler(TemplateEngine templateEngine, TrackerService trackerService) {
        this.templateEngine = templateEngine;
        this.trackerService = trackerService;
    }

    public ServerResponse viewTrackers(ServerRequest req) {
        List<Tracker> trackers = trackerService.listTrackers();
        TemplateOutput output = new StringOutput();
        templateEngine.render("trackers.jte", trackers, output);
        return ServerResponse.ok().header("content-type", "text/html").body(output.toString());
    }
}
