package com.example.hadconsentservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.twilio.Twilio;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HadConsentServiceApplication {
	// @Value("${TWILIO_ACCOUNT_SID}")
	// static String accountSid;

	// @Value("${TWILIO_AUTH_KEY}")
	// static String authKey;

	public static void main(String[] args) {
		// System.out.println(accountSid);
		// Twilio.init(accountSid, authKey);
		SpringApplication.run(HadConsentServiceApplication.class, args);
	}

}
