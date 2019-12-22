package com.exercise.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoyaltyProgramApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoyaltyProgramApplication.class, args);
	}
}
