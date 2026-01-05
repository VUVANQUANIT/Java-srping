package com.example.JavaSpring1;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class DatabaseChecker {

    private final DataSource dataSource;

    @PostConstruct
    public void check() {
        try {
            System.out.println("✅ DB URL: " + dataSource.getConnection().getMetaData().getURL());
        } catch (Exception e) {
            System.err.println("❌ Cannot connect DB");
            e.printStackTrace();
        }
    }
}

