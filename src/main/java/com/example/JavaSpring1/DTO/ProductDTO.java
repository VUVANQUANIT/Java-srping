package com.example.JavaSpring1.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

@Data
public class ProductDTO {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private Double price;
    @Size(max = 30)
    @Positive(message = "Mô tả không được quá 30 kí tự")
    private String description;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 0, message = "Số lượng sản phẩm không được âm")
    private Integer quantity;
}
