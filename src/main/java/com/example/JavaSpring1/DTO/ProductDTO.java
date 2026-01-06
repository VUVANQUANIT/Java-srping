package com.example.JavaSpring1.DTO;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private double price;
    private String description;
    private int quantity;
}
