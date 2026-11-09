package com.bydrim.hollingate.configs;

import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * Calculates properties to be used at early stages, such as in application.yaml.
 * Registered at META-INF/spring-factories.
 */
public class EnvPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // its value is project dir if run from IDE, or dir of the jar if the jar is run.
        environment.getPropertySources().addLast(new MapPropertySource("calculatedProps",
                Map.of("appHome", new ApplicationHome().getDir().toString())));
    }

    @Override
    public int getOrder() {
        return ConfigDataEnvironmentPostProcessor.ORDER - 1;
    }
}
