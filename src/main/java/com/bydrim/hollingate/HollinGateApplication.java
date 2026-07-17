package com.bydrim.hollingate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({ "com.bydrim.hollingate.configs" })
public class HollinGateApplication {

	public static void main(String[] args) {
        SpringApplication.run(HollinGateApplication.class, args);
	}

}
