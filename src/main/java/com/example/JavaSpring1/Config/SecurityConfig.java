package com.example.JavaSpring1.Config;

import com.example.JavaSpring1.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )// Tắt CSRF cho API testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Cho phép các endpoint auth không cần authentication
                .requestMatchers("GET", "/api/product").permitAll() // Cho phép GET /api/product không cần authentication
                .requestMatchers("GET", "/api/product/{id}").permitAll() // Cho phép GET /api/product/{id} không cần authentication
                .requestMatchers("/api/**").authenticated() // Các endpoint khác của /api/** cần authentication (sẽ được kiểm tra bởi @PreAuthorize)
                .anyRequest().authenticated() // Các request khác vẫn cần authentication
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}

