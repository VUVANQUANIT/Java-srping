package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.DTO.LoginRequest;
import com.example.JavaSpring1.DTO.RegisterRequest;
import com.example.JavaSpring1.Entity.User;
import com.example.JavaSpring1.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(encodedPassword);
        userRepository.save(user);
        log.info("User registered: {}", user.getEmail());
    }
    public void login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc password sai"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Email hoặc password sai");
        }
        log.info("User login success: {}", user.getEmail());
    }

}
