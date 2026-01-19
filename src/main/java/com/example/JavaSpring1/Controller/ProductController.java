package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.ErrorResponse;
import com.example.JavaSpring1.DTO.ProductDTO;
import com.example.JavaSpring1.Entity.Product;
import com.example.JavaSpring1.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
@Tag(name = "Product", description = "API quản lý sản phẩm")
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Lấy danh sách sản phẩm",
            description = "Lấy danh sách tất cả sản phẩm với phân trang. Endpoint này public, không cần authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = Page.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProducts(
            @Parameter(description = "Thông tin phân trang (page, size, sort)")
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @Operation(
            summary = "Tạo sản phẩm mới",
            description = "Tạo một sản phẩm mới. Cần authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tạo sản phẩm thành công",
                    content = @Content(schema = @Schema(implementation = Product.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa đăng nhập",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("create")
    public ResponseEntity<Product> createProduct(
            @Valid @RequestBody ProductDTO product
    ) {
        Product createdProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @Operation(
            summary = "Lấy thông tin sản phẩm theo ID",
            description = "Lấy chi tiết một sản phẩm. Endpoint này public, không cần authentication."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy thông tin thành công",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy sản phẩm",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductsByID(
            @Parameter(description = "ID của sản phẩm", required = true)
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(
            summary = "Cập nhật sản phẩm",
            description = "Cập nhật thông tin sản phẩm. Chỉ ADMIN mới có quyền.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật thành công",
                    content = @Content(schema = @Schema(implementation = ProductDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa đăng nhập",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền (chỉ ADMIN)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy sản phẩm",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID của sản phẩm", required = true)
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductDTO productDTO
    ) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @Operation(
            summary = "Xóa sản phẩm",
            description = "Xóa một sản phẩm. Cần có role USER hoặc ADMIN.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Xóa thành công"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa đăng nhập",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy sản phẩm",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID của sản phẩm", required = true)
            @PathVariable("id") Long id
    ) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

