package com.bydrim.hollingate.configs;

import com.bydrim.hollingate.entities.Tracker;
import com.bydrim.hollingate.services.TrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class Startup {
    private static final Logger logger = LoggerFactory.getLogger(Startup.class);

    @Bean
    public CommandLineRunner addRunner(TrackerService trackerService) {
        return (String[] args) -> {
            if(trackerService.isEmpty()) {
                Tracker t1 = trackerService.saveNewTracker("Test-1 tracker");
                Tracker t2 = trackerService.saveNewTracker("Test-2 tracker");

                trackerService.saveNewTrigger(t1, new URI("http://test-1.com").toURL());
                trackerService.saveNewTrigger(t2, new URI("http://test-2.com").toURL());

                logger.info("Created 2 test trackers and a single trigger for each tracker.");
            }
        };
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> addListener(
            GatewayConfig gatewayConfig, TrackerService trackerService) {
        return (ApplicationReadyEvent event) -> {
            logger.info("hollin-gate.yaml gateway config:\n{}", gatewayConfig);
            logger.info("Last 3 tracker: \n{}", String.join("\n", trackerService.findLast(3).stream().map(Tracker::toString).toList()));
        };
    }
}
