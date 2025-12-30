package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.ENUM.OrderStatus;
import com.example.JavaSpring1.Entity.StartupLogger;
import com.example.JavaSpring1.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final StartupLogger startupLogger;
    @GetMapping
    public List<OrderResponseDTO> getOrders() {
        return orderService.getAllOrders();
    }
    @GetMapping("/revenue")
    public Map<String,Double> getRevenue() {
        double revenue = orderService.totalAmount();
        return Map.of("revenue",revenue);
    }
    @GetMapping("/max")
    public OrderResponseDTO getMax() {
        return orderService.getOrderMax();
    }
    @GetMapping("/high-value")
    public List<OrderResponseDTO> getHighValue(@RequestParam double minvalue) {
        return orderService.getOrderMaxPaid(minvalue);
    }
    @GetMapping("/sorted")
    public List<OrderResponseDTO> getSorted() {
        return orderService.sortByAmountDesc1();
    }
    @GetMapping("/stats")
    public Map<OrderStatus,Long> getOderStats() {
        return orderService.countByStatus();
    }
}
