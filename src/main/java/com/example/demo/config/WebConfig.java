package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure resource handler for images
        registry.addResourceHandler("/videofiles/**")
                .addResourceLocations("file:/pleiades/2023120614/workspace/members/videofiles/"); // Replace with your actual path
    }
}