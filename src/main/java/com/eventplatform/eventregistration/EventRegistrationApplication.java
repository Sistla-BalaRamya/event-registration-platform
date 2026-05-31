package com.eventplatform.eventregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.eventplatform")
public class EventRegistrationApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventRegistrationApplication.class, args);
	}
}