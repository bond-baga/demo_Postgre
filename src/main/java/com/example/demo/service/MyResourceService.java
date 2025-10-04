package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class MyResourceService {

    @Autowired
    private ResourceLoader resourceLoader;

    public Resource loadResource(String location) {
        return resourceLoader.getResource("classpath:videofilesss");
    }
}
