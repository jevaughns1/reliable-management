package com.skillstorm.reliable_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.skillstorm.reliable_api.repositories")
public class ReliableApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReliableApiApplication.class, args);
	}

	

}
