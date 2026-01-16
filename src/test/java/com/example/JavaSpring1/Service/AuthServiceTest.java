package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.Config.JwtConfig;
import com.example.JavaSpring1.DTO.LoginRequest;
import com.example.JavaSpring1.DTO.LoginResponse;
import com.example.JavaSpring1.DTO.RegisterRequest;
import com.example.JavaSpring1.ENUM.Role;
import com.example.JavaSpring1.Entity.User;
import com.example.JavaSpring1.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Setup test data
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("12345678");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("12345678");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@gmail.com");
        testUser.setPasswordHash("hashed_password");
        testUser.setRole(Role.USER);
    }

    @Test
    @DisplayName("Đăng ký thành công - Email chưa tồn tại")
    void register_Success_WhenEmailNotExists() {
        // GIVEN - Chuẩn bị dữ liệu
        when(userRepository.existsByEmail(registerRequest.getEmail()))
                .thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("hashed_password");

        // WHEN - Thực hiện hành động
        authService.register(registerRequest);

        // THEN - Kiểm tra kết quả
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(registerRequest.getEmail(), savedUser.getEmail());
        assertEquals("hashed_password", savedUser.getPasswordHash());
        assertEquals(Role.USER, savedUser.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Đăng ký thất bại - Email đã tồn tại")
    void register_Fail_WhenEmailExists() {
        // GIVEN
        when(userRepository.existsByEmail(registerRequest.getEmail()))
                .thenReturn(true);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Email đã tồn tại", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Đăng nhập thành công - Email và password đúng")
    void login_Success_WhenCredentialsAreValid() {
        // GIVEN
        String expectedToken = "jwt_token_123";
        Long expiration = 3600000L; // 1 hour in milliseconds

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPasswordHash()))
                .thenReturn(true);
        when(jwtService.generateToken(testUser))
                .thenReturn(expectedToken);
        when(jwtConfig.getExpiration())
                .thenReturn(expiration);

        // WHEN
        LoginResponse response = authService.login(loginRequest);

        // THEN
        assertNotNull(response);
        assertEquals(expectedToken, response.getAccess_token());
        assertEquals("Bearer", response.getToken_type());
        assertEquals(expiration / 1000, response.getExpires_in());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), testUser.getPasswordHash());
        verify(jwtService, times(1)).generateToken(testUser);
    }

    @Test
    @DisplayName("Đăng nhập thất bại - Email không tồn tại")
    void login_Fail_WhenEmailNotFound() {
        // GIVEN
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Email hoặc password sai", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("Đăng nhập thất bại - Password sai")
    void login_Fail_WhenPasswordIsWrong() {
        // GIVEN
        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPasswordHash()))
                .thenReturn(false);

        // WHEN & THEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Email hoặc password sai", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(loginRequest.getPassword(), testUser.getPasswordHash());
        verify(jwtService, never()).generateToken(any(User.class));
    }
}
