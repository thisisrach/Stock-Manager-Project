package com.ofss;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/customers/**")
                        .allowedOrigins("http://localhost:8000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");

                registry.addMapping("/transactions/**")
                        .allowedOrigins("http://localhost:8000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");

                registry.addMapping("/stocks/**")
                        .allowedOrigins("http://localhost:8000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");

                registry.addMapping("/api/users/**") 
                        .allowedOrigins("http://localhost:8000")
                        .allowedMethods("GET", "POST", "OPTIONS");
                registry.addMapping("/api/customer/**") 
                		.allowedOrigins("http://localhost:8000")
                		.allowedMethods("GET", "POST", "OPTIONS");
                registry.addMapping("/api/stocks/**") 
        				.allowedOrigins("http://localhost:8000")
        				.allowedMethods("GET", "POST", "OPTIONS");
                registry.addMapping("/api/**") 
						.allowedOrigins("http://localhost:8000")
						.allowedMethods("GET", "POST", "OPTIONS");
              
            }
        };
    }
}
