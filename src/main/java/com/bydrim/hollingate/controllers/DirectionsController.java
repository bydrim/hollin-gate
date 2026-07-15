package com.bydrim.hollingate.controllers;

import java.util.List;

import com.bydrim.hollingate.configs.GatewayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DirectionsController {
    private final Logger log = LoggerFactory.getLogger(DirectionsController.class);
    private final GatewayConfig gatewayConfig;

    public DirectionsController(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    @GetMapping("/directions")
    public String directions(Model model) {
//        List<String> directions = List.of("test-1", "test-2");
        model.addAttribute("directions", gatewayConfig.directions());
        return "directions";
    }
}
