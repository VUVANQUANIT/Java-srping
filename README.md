# JavaSpring1 - API Quản Lý Đơn Hàng và Sản Phẩm

REST API sử dụng Spring Boot, PostgreSQL, JWT Authentication, và Swagger UI.

## Tính Năng

- ✅ JWT Authentication (đăng ký, đăng nhập)
- ✅ Quản lý sản phẩm (CRUD)
- ✅ Quản lý đơn hàng
- ✅ Role-based access control (USER, ADMIN)
- ✅ Swagger UI để test API
- ✅ Validation
- ✅ Global Exception Handling
- ✅ Unit Tests

## Tech Stack

- **Framework**: Spring Boot 3.5.9
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Maven
- **ORM**: Hibernate/JPA
- **Testing**: JUnit 5, Mockito

## Khởi Động Nhanh

### 1. Clone & Setup

```bash
git clone <your-repo>
cd JavaSpring1
```

### 2. Khởi động PostgreSQL

```bash
docker run --name postgres-db \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=orders_db \
  -p 5432:5432 \
  -d postgres:latest
```

### 3. Chạy Ứng Dụng

```bash
mvn spring-boot:run
```

### 4. Truy cập Swagger UI

Mở trình duyệt: **http://localhost:8080/swagger-ui/index.html**

## Cấu Trúc Project

```
src/
├── main/
│   ├── java/com/example/JavaSpring1/
│   │   ├── Config/           # Cấu hình (Security, JWT, OpenAPI)
│   │   ├── Controller/       # REST Controllers
│   │   ├── DTO/              # Data Transfer Objects
│   │   ├── Entity/           # JPA Entities
│   │   ├── ENUM/             # Enumerations
│   │   ├── Exception/        # Exception Handlers
│   │   ├── Repository/       # JPA Repositories
│   │   ├── Service/          # Business Logic
│   │   ├── mappers/          # MapStruct Mappers
│   │   └── security/         # JWT Filter
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/JavaSpring1/
        ├── Controller/       # Controller Tests
        └── Service/          # Service Tests
```

## API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Đăng ký tài khoản | No |
| POST | `/auth/login` | Đăng nhập | No |

### Products
| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| GET | `/api/product` | Danh sách sản phẩm | No | - |
| GET | `/api/product/{id}` | Chi tiết sản phẩm | No | - |
| POST | `/api/product/create` | Tạo sản phẩm | Yes | USER/ADMIN |
| PUT | `/api/product/{id}` | Cập nhật sản phẩm | Yes | ADMIN |
| DELETE | `/api/product/{id}` | Xóa sản phẩm | Yes | USER/ADMIN |

## Test API với Swagger

1. Truy cập: http://localhost:8080/swagger-ui/index.html
2. Đăng ký tài khoản qua `/auth/register`
3. Đăng nhập qua `/auth/login` → lấy token
4. Click nút "Authorize" → paste token
5. Test các API khác

**Chi tiết:** Xem [SWAGGER_GUIDE.md](./SWAGGER_GUIDE.md)

## Chạy Tests

```bash
# Tất cả tests
mvn test

# Test cụ thể
mvn test -Dtest=AuthServiceTest
mvn test -Dtest=AuthControllerTest
```

## Cấu Hình

File `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/orders_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=9f3eA!d0XK2pL8QmZrT1U#WcVYJ@B7Hs
jwt.expiration=86400000
jwt.refresh-expiration=604800000

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
```

## Troubleshooting

### Lỗi: Connection refused (PostgreSQL)

```bash
# Kiểm tra PostgreSQL
docker ps | grep postgres

# Start lại nếu cần
docker start postgres-db
```

### Lỗi: Port 8080 already in use

Thay đổi port trong `application.properties`:
```properties
server.port=8081
```

### Lỗi: 401 Unauthorized

- Token hết hạn → đăng nhập lại
- Chưa authorize → click "Authorize" trong Swagger

## Environment Variables (Optional)

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=orders_db
export DB_USER=postgres
export DB_PASSWORD=postgres
export JWT_SECRET=your-secret-key
```

## Docker Support

```bash
# Build image
docker build -t javaspring1 .

# Run container
docker run -p 8080:8080 javaspring1
```

## Roadmap

- [ ] Refresh Token
- [ ] Email Verification
- [ ] File Upload
- [ ] Pagination Enhancement
- [ ] Caching with Redis
- [ ] API Rate Limiting
- [ ] Metrics với Prometheus

## Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License.

## Contact

Your Name - your.email@example.com

Project Link: [https://github.com/yourusername/JavaSpring1](https://github.com/yourusername/JavaSpring1)

---

**Happy Coding! 🚀**
