
/*
 * SwaggerConfig.java
 * 
 * This configuration class sets up Swagger for API documentation. It defines the OpenAPI bean which includes metadata about the API such as title, description, and version. This allows for easy generation of API documentation and testing through Swagger UI.
 */

package edu.log.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LogSys API")
                        .description("API for LogSys application")
                        .version("1.0.0"));
    }
}
