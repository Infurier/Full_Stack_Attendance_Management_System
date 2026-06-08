package com.example.attendance_management.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@SuppressWarnings("unused")
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000") // React dev server
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

