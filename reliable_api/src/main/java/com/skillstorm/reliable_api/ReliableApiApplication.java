package com.skillstorm.reliable_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main entry point for the Reliable API Spring Boot application.
 * <p>
 * This class initializes and runs the Spring application context, enabling auto-configuration,
 * JPA auditing, and specifying the base package for JPA repositories.
 * </p>
 *
 * @author Jevaughn Stewart
 * @version 1.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.skillstorm.reliable_api.repositories")
public class ReliableApiApplication {

    /**
     * The main method that starts the Spring Boot application.
     * * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(ReliableApiApplication.class, args);
    }

    

}