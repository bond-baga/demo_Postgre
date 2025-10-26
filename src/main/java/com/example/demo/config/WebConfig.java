package com.example.demo.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure resource handler for images0
        registry.addResourceHandler("/videos/**")
        .addResourceLocations("classpath:/videos/"); // Replace with your actual path

//        registry.addResourceHandler("/publish2/**")
//        .addResourceLocations("file:///C:/pleiades/2023120614/workspace/demo_Postgre/videofiles/"); // Replace with your actual path
 
        
        Path videofiles = Paths.get( Paths.get(".").normalize().toAbsolutePath().toString(),"videofiles");        
        String tmp = videofiles.toString().replace("\\", "/");
        registry.addResourceHandler("/videofiles/**")
        .addResourceLocations("file:/" + tmp + "/"); // Replace with your actual path
        
        registry.addResourceHandler("/images/**")
		.addResourceLocations("classpath:/static/images/"); // Replace with your actual path
    }
}