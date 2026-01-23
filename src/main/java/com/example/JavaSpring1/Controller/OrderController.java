package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.ENUM.OrderStatus;
import com.example.JavaSpring1.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Double>> getRevenue() {
        double revenue = orderService.totalAmount();
        return ResponseEntity.ok(Map.of("revenue", revenue));
    }

    @GetMapping("/max")
    public ResponseEntity<OrderResponseDTO> getMax() {
        OrderResponseDTO order = orderService.getOrderMax();
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/max-paid")
    public ResponseEntity<OrderResponseDTO> getMaxPaid() {
        OrderResponseDTO order = orderService.getOrderMaxPaid();
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/high-value")
    public ResponseEntity<List<OrderResponseDTO>> getHighValue(@RequestParam double minvalue) {
        return ResponseEntity.ok(orderService.getOrderMaxPaid(minvalue));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<OrderResponseDTO>> getSorted() {
        return ResponseEntity.ok(orderService.sortByAmountDesc());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<OrderStatus, Long>> getOrderStats() {
        return ResponseEntity.ok(orderService.countByStatus());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByUserEmail(email));
    }
}
