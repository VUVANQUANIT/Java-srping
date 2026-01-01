package com.example.JavaSpring1.DTO;

import com.example.JavaSpring1.ENUM.OrderStatus;
import lombok.Data;

@Data
public class OrderResponseDTO {
    private String customerName;
    private double amount;
    private OrderStatus status;
}
