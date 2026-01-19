package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.ErrorResponse;
import com.example.JavaSpring1.DTO.LoginRequest;
import com.example.JavaSpring1.DTO.LoginResponse;
import com.example.JavaSpring1.DTO.RegisterRequest;
import com.example.JavaSpring1.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API xác thực người dùng (đăng ký, đăng nhập)")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Đăng ký tài khoản mới",
            description = "Tạo tài khoản người dùng mới với email và password"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Đăng ký thành công",
                    content = @Content(schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ hoặc email đã tồn tại",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Register OK");
    }

    @Operation(
            summary = "Đăng nhập",
            description = "Đăng nhập bằng email và password để lấy JWT token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Đăng nhập thành công, trả về JWT token",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email hoặc password không đúng",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}

