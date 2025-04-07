/*
 * LogisticSystemService.java
 * 
 * This is the main entry point for the Logistic System Service application. It uses Spring Boot to bootstrap the application. The class extends SpringBootServletInitializer to support deployment as a WAR file in a servlet container.
 */

package edu.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class LogisticSystemService extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(LogisticSystemService.class, args);
    }
}
