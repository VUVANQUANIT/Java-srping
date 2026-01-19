# Hướng Dẫn Sử Dụng Swagger UI

## Cấu Hình Đã Hoàn Tất ✅

Swagger/OpenAPI đã được cấu hình xong cho ứng dụng JavaSpring1. Các thay đổi bao gồm:

### 1. Dependencies (pom.xml)
- Đã thêm `springdoc-openapi-starter-webmvc-ui` version 2.8.6

### 2. OpenAPI Configuration (OpenApiConfig.java)
- Cấu hình thông tin API (title, description, contact, license)
- Cấu hình JWT Bearer Authentication
- Thêm 2 servers: localhost và production

### 3. Security Configuration (SecurityConfig.java)
- Cho phép truy cập Swagger UI không cần authentication:
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/swagger-ui.html`

### 4. Controller Annotations
- Đã thêm Swagger annotations cho:
  - `AuthController`: API đăng ký và đăng nhập
  - `ProductController`: API quản lý sản phẩm

### 5. Application Properties
- Cấu hình đường dẫn và tùy chọn Swagger

## Cách Khởi Động Ứng Dụng

### Bước 1: Khởi động PostgreSQL

Ứng dụng cần PostgreSQL để chạy. Có 2 cách:

#### Cách 1: Sử dụng Docker (Khuyến nghị)

```bash
docker run --name postgres-db -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=orders_db -p 5432:5432 -d postgres:latest
```

#### Cách 2: Cài đặt PostgreSQL trực tiếp
- Download và cài đặt PostgreSQL từ [postgresql.org](https://www.postgresql.org/download/)
- Tạo database tên `orders_db`
- Username: `postgres`, Password: `postgres`

### Bước 2: Chạy Ứng Dụng Spring Boot

```bash
# Sử dụng Maven
mvn spring-boot:run

# Hoặc sử dụng Maven Wrapper (Windows)
mvnw.cmd spring-boot:run
```

### Bước 3: Truy Cập Swagger UI

Sau khi ứng dụng khởi động thành công, mở trình duyệt và truy cập:

**Swagger UI**: http://localhost:8080/swagger-ui/index.html

**API Docs (JSON)**: http://localhost:8080/v3/api-docs

## Cách Sử Dụng Swagger UI

### 1. Test API Không Cần Authentication

Các endpoint public (không cần đăng nhập):
- `POST /auth/register` - Đăng ký tài khoản mới
- `POST /auth/login` - Đăng nhập
- `GET /api/product` - Lấy danh sách sản phẩm
- `GET /api/product/{id}` - Lấy chi tiết sản phẩm

**Cách test:**
1. Click vào endpoint muốn test
2. Click nút "Try it out"
3. Nhập dữ liệu (nếu cần)
4. Click "Execute"
5. Xem kết quả ở phần "Responses"

### 2. Test API Cần Authentication

#### Bước 1: Lấy JWT Token

1. Đăng ký tài khoản mới qua `POST /auth/register`:
```json
{
  "email": "test@gmail.com",
  "password": "12345678"
}
```

2. Đăng nhập qua `POST /auth/login`:
```json
{
  "email": "test@gmail.com",
  "password": "12345678"
}
```

3. Copy `access_token` từ response

#### Bước 2: Authorize

1. Click nút **"Authorize"** ở góc trên bên phải (biểu tượng ổ khóa)
2. Paste token vào ô "Value" (KHÔNG cần thêm "Bearer ")
3. Click "Authorize"
4. Click "Close"

#### Bước 3: Test API

Giờ bạn có thể test các endpoint cần authentication:
- `POST /api/product/create` - Tạo sản phẩm mới
- `PUT /api/product/{id}` - Cập nhật sản phẩm (chỉ ADMIN)
- `DELETE /api/product/{id}` - Xóa sản phẩm (USER hoặc ADMIN)

## Ví Dụ Test Flow

### Scenario 1: Đăng ký và đăng nhập

1. **Đăng ký**:
   - Endpoint: `POST /auth/register`
   - Body:
   ```json
   {
     "email": "user@example.com",
     "password": "password123"
   }
   ```
   - Response: `"Register OK"`

2. **Đăng nhập**:
   - Endpoint: `POST /auth/login`
   - Body:
   ```json
   {
     "email": "user@example.com",
     "password": "password123"
   }
   ```
   - Response:
   ```json
   {
     "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6...",
     "token_type": "Bearer",
     "expires_in": 86400
   }
   ```

### Scenario 2: Quản lý sản phẩm

1. **Lấy danh sách sản phẩm** (không cần auth):
   - Endpoint: `GET /api/product`
   - Parameters: page=0, size=10

2. **Tạo sản phẩm mới** (cần auth):
   - Authorize với token từ login
   - Endpoint: `POST /api/product/create`
   - Body:
   ```json
   {
     "name": "iPhone 15",
     "description": "Latest iPhone model",
     "price": 999.99,
     "stockQuantity": 50
   }
   ```

3. **Cập nhật sản phẩm** (cần ADMIN role):
   - Endpoint: `PUT /api/product/{id}`
   - Body:
   ```json
   {
     "name": "iPhone 15 Pro",
     "description": "Pro model with better camera",
     "price": 1199.99,
     "stockQuantity": 30
   }
   ```

## Troubleshooting

### Lỗi: "Connection refused" hoặc không khởi động được

**Nguyên nhân**: PostgreSQL chưa chạy

**Giải pháp**:
1. Kiểm tra PostgreSQL có đang chạy không:
   ```bash
   # Windows
   Get-Service postgresql*
   
   # Hoặc check port 5432
   netstat -an | findstr 5432
   ```

2. Khởi động PostgreSQL:
   ```bash
   # Nếu dùng Docker
   docker start postgres-db
   
   # Hoặc start service
   net start postgresql-x64-{version}
   ```

### Lỗi: "Email đã tồn tại"

**Nguyên nhân**: Email đã được đăng ký trước đó

**Giải pháp**: Sử dụng email khác hoặc test với endpoint login

### Lỗi: 401 Unauthorized

**Nguyên nhân**: Chưa authenticate hoặc token hết hạn

**Giải pháp**:
1. Click "Authorize" và nhập token mới
2. Hoặc đăng nhập lại để lấy token mới

### Lỗi: 403 Forbidden

**Nguyên nhân**: Không đủ quyền (ví dụ: update product cần role ADMIN)

**Giải pháp**: 
- Sử dụng tài khoản có role phù hợp
- Hiện tại user mới tạo có role USER, cần ADMIN role để update/delete

## Tính Năng Swagger

### 1. Try It Out
- Test API trực tiếp từ browser
- Không cần Postman hay công cụ khác

### 2. Request/Response Schema
- Xem cấu trúc request body
- Xem cấu trúc response
- Validation rules (required, min/max length, etc.)

### 3. Authentication
- Hỗ trợ JWT Bearer token
- Dễ dàng authorize một lần cho tất cả API

### 4. Export
- Download OpenAPI spec (JSON/YAML)
- Import vào Postman hoặc tools khác

## Các URL Quan Trọng

| Mô tả | URL |
|-------|-----|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| API Docs (JSON) | http://localhost:8080/v3/api-docs |
| API Docs (YAML) | http://localhost:8080/v3/api-docs.yaml |
| Application | http://localhost:8080 |

## Lưu Ý Quan Trọng

1. **Token Format**: Khi authorize, CHỈ paste token, KHÔNG thêm "Bearer " phía trước
2. **Token Expiration**: Token có thời hạn 24 giờ (86400 giây), sau đó cần đăng nhập lại
3. **Role-Based Access**: 
   - USER: có thể tạo và xóa product
   - ADMIN: có thể update product
4. **Validation**: API có validation, đảm bảo nhập đúng định dạng:
   - Email phải hợp lệ
   - Password tối thiểu 8 ký tự

## Tài Liệu Tham Khảo

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [Swagger UI Guide](https://swagger.io/tools/swagger-ui/)

---

**Chúc bạn test API thành công! 🚀**

Nếu gặp vấn đề, hãy kiểm tra:
1. PostgreSQL đã chạy chưa
2. Port 8080 có bị chiếm bởi ứng dụng khác không
3. Xem log trong console để biết lỗi cụ thể
