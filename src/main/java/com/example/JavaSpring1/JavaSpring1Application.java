package com.example.JavaSpring1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class JavaSpring1Application {

	public static void main(String[] args) {
		// Force JVM timezone to UTC to prevent PostgreSQL timezone issues
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("user.timezone", "UTC");
		
		SpringApplication.run(JavaSpring1Application.class, args);
	}

}
