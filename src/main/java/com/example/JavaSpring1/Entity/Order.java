package com.example.JavaSpring1.Entity;

import com.example.JavaSpring1.ENUM.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Order {
    @Id
    private Long id;
    private String customerName;
    private double amount;
    private OrderStatus status;
}
