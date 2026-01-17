package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.LoginRequest;
import com.example.JavaSpring1.DTO.LoginResponse;
import com.example.JavaSpring1.DTO.RegisterRequest;
import com.example.JavaSpring1.Service.AuthService;
import com.example.JavaSpring1.Service.CustomUserDetailsService;
import com.example.JavaSpring1.Service.JwtService;
import com.example.JavaSpring1.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        // Setup valid register request
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setEmail("test@gmail.com");
        validRegisterRequest.setPassword("12345678");

        // Setup valid login request
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@gmail.com");
        validLoginRequest.setPassword("12345678");

        // Setup login response
        loginResponse = new LoginResponse(
                "fake-jwt-token-123",
                "Bearer",
                3600L
        );
    }

    // ========== REGISTER TESTS ==========

    @Test
    @DisplayName("POST /auth/register - Thành công với dữ liệu hợp lệ")
    void register_Success_WhenValidRequest() throws Exception {
        // GIVEN
        doNothing().when(authService).register(any(RegisterRequest.class));

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Register OK"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi email trống")
    void register_Fail_WhenEmailIsBlank() throws Exception {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("");
        request.setPassword("12345678");

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("email"));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi email không hợp lệ")
    void register_Fail_WhenEmailIsInvalid() throws Exception {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("invalid-email");
        request.setPassword("12345678");

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("email"));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi password trống")
    void register_Fail_WhenPasswordIsBlank() throws Exception {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("");

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("password"));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi password quá ngắn")
    void register_Fail_WhenPasswordTooShort() throws Exception {
        // GIVEN
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("1234567"); // < 8 characters

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("password"));

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi email đã tồn tại")
    void register_Fail_WhenEmailAlreadyExists() throws Exception {
        // GIVEN
        doThrow(new IllegalArgumentException("Email đã tồn tại"))
                .when(authService).register(any(RegisterRequest.class));

        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Email đã tồn tại"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /auth/register - Thất bại khi thiếu request body")
    void register_Fail_WhenMissingRequestBody() throws Exception {
        // WHEN & THEN
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(authService, never()).register(any(RegisterRequest.class));
    }

    // ========== LOGIN TESTS ==========

    @Test
    @DisplayName("POST /auth/login - Thành công với credentials hợp lệ")
    void login_Success_WhenValidCredentials() throws Exception {
        // GIVEN
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value("fake-jwt-token-123"))
                .andExpect(jsonPath("$.token_type").value("Bearer"))
                .andExpect(jsonPath("$.expires_in").value(3600));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi email trống")
    void login_Fail_WhenEmailIsBlank() throws Exception {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("12345678");

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("email"));

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi email không hợp lệ")
    void login_Fail_WhenEmailIsInvalid() throws Exception {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid-email");
        request.setPassword("12345678");

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("email"));

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi password trống")
    void login_Fail_WhenPasswordIsBlank() throws Exception {
        // GIVEN
        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("");

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors[0].field").value("password"));

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi email không tồn tại")
    void login_Fail_WhenEmailNotFound() throws Exception {
        // GIVEN
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Email hoặc password sai"));

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Email hoặc password sai"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi password sai")
    void login_Fail_WhenPasswordIsWrong() throws Exception {
        // GIVEN
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new IllegalArgumentException("Email hoặc password sai"));

        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Email hoặc password sai"));

        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi thiếu request body")
    void login_Fail_WhenMissingRequestBody() throws Exception {
        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /auth/login - Thất bại khi JSON không hợp lệ")
    void login_Fail_WhenInvalidJson() throws Exception {
        // WHEN & THEN
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ invalid json }"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequest.class));
    }
}
