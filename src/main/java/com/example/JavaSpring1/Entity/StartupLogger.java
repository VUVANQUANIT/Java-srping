package com.example.JavaSpring1.Entity;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger {
    public StartupLogger() {
        System.out.println("Constructor called");
    }

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct called");
    }
}
