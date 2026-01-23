package com.example.JavaSpring1.DTO;

import com.example.JavaSpring1.ENUM.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private double amount;
    private OrderStatus status;
    // User information
    private Long userId;
    private String userEmail;
}
