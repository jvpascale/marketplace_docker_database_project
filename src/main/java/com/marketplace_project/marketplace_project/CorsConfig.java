package com.marketplace_project.marketplace_project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Adicione também o 127.0.0.1, pois alguns navegadores alternam entre ele e localhost
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT")
                // PERMITE TODOS OS HEADERS (Essencial se você manda JSON ou Tokens)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
