package com.ANP.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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

    @Bean
    public RestTemplate restTemplate() {

        System.out.println("Creating rest Template ");

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(factory);
        System.out.println("Creating rest Template " + restTemplate);

        return restTemplate;

    }



}
