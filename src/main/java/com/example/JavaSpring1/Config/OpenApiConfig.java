package com.example.JavaSpring1.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình OpenAPI (Swagger) cho ứng dụng
 * Truy cập Swagger UI tại: http://localhost:8080/swagger-ui/index.html
 * Truy cập API Docs tại: http://localhost:8080/v3/api-docs
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "JavaSpring1 API",
                version = "1.0.0",
                description = """
                        API quản lý đơn hàng và sản phẩm
                        
                        **Hướng dẫn sử dụng:**
                        1. Đăng ký tài khoản: POST /auth/register
                        2. Đăng nhập: POST /auth/login để lấy JWT token
                        3. Click nút "Authorize" ở trên và nhập token (không cần thêm "Bearer ")
                        4. Bây giờ bạn có thể gọi các API cần authentication
                        """,
                contact = @Contact(
                        name = "Support Team",
                        email = "support@example.com",
                        url = "https://github.com/yourusername"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                ),
                @Server(
                        url = "https://api.production.com",
                        description = "Production Server"
                )
        },
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "Nhập JWT token (không cần thêm 'Bearer ' vào đầu)",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuration is done via annotations
}
