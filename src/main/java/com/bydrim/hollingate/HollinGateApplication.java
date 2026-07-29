package com.bydrim.hollingate;


import com.bydrim.hollingate.configs.GatewayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({ "com.bydrim.hollingate.configs" })
public class HollinGateApplication {
	private static final Logger logger = LoggerFactory.getLogger(HollinGateApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HollinGateApplication.class);
		app.addListeners((ApplicationReadyEvent event) -> {
			GatewayConfig conf = event.getApplicationContext().getBean(GatewayConfig.class);
			logger.info("hollin-gate.yaml gateway config:\n{}", conf);
		});
		app.run(args);
	}

}
