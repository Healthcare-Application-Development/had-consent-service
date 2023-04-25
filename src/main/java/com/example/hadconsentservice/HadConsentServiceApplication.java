package com.example.hadconsentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HadConsentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HadConsentServiceApplication.class, args);
	}

}
