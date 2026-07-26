package com.bydrim.hollingate;


import com.bydrim.hollingate.configs.GatewayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({ "com.bydrim.hollingate.configs" })
public class HollinGateApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HollinGateApplication.class);
		app.addListeners((ApplicationReadyEvent event) -> {
			GatewayConfig conf = event.getApplicationContext().getBean(GatewayConfig.class);
			// TODO: use logging instead of console login
			System.out.println(conf);
		});
		app.run(args);
	}

}
