package com.ANP.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication (scanBasePackages = { "com.ANP" })
public class ApplicationStarter extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApplicationStarter.class);
	}

	public static void main(String[] args) {
		System.out.println("System Property Test: " + System.getProperty("TestModeOTP"));
		SpringApplication.run(ApplicationStarter.class, args);
	}


}