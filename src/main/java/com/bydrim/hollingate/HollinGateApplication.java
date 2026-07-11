package com.bydrim.hollingate;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan({ "com.bydrim.hollingate.configs" })
public class HollinGateApplication {

	public static void main(String[] args) {
		// create db file if not exists
//		try{
//			var parentPath = new File(HollinGateApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getAbsolutePath();
//			var isCreated = new File(parentPath + "/hollin-gate.db").createNewFile();
//			System.out.println(isCreated ? "hollin-gate.db file has created anew." : "hollin-gate.db file already exists.");
//        } catch (Exception e) {
//			throw new RuntimeException(e);
//		}
        SpringApplication.run(HollinGateApplication.class, args);
	}

}
