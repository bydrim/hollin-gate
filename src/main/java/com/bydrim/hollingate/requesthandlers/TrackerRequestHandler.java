package com.bydrim.hollingate.requesthandlers;

import com.bydrim.hollingate.entities.Tracker;
import com.bydrim.hollingate.exceptions.TooManyTrialException;
import com.bydrim.hollingate.services.TrackerService;
import gg.jte.TemplateEngine;
import gg.jte.TemplateOutput;
import gg.jte.output.StringOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class TrackerRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(TrackerRequestHandler.class);
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

    public ServerResponse createTracker(ServerRequest req) {
        try {
            MultiValueMap<String, String> form = UriComponentsBuilder
                    .newInstance()
                    .query(req.servletRequest().getReader().readAllAsString())
                    .build()
                    .getQueryParams();
            Optional<String> desc = Optional.ofNullable(form.getFirst("description"));
            if (desc.isEmpty()) {
                return ServerResponse.badRequest().body("Error: Description of a Tracker cannot be empty!");
            }
            Tracker createdTracker = trackerService.saveNewTracker(desc.get());

            List<Tracker> trackers = trackerService.listTrackers();
            TemplateOutput output = new StringOutput();
            templateEngine.render("trackers.jte", trackers, output);
            return ServerResponse.created(req.uri().resolve(createdTracker.getId())).header("content-type", "text/html").body(output.toString());
        } catch (IOException e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (TooManyTrialException e) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ServerResponse deleteTracker(ServerRequest req) {
        String id = req.pathVariable("id");
        trackerService.delete(id);
        return ServerResponse.ok().body("deleted");
    }
}
