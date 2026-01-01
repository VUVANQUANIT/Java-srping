package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.OrderRequestDTO;
import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.ENUM.OrderStatus;
import com.example.JavaSpring1.Entity.StartupLogger;
import com.example.JavaSpring1.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
   // private final StartupLogger startupLogger;
    @GetMapping("/list")
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
    @PostMapping("create")
    public List<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
                return orderService.getAllOrders();
    }
    @PutMapping("/{id}")
    public OrderResponseDTO updateOrder(@PathVariable Integer id, @RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.updateOrder(id,orderRequestDTO);
    }
    @DeleteMapping("/{id}")
    public List<OrderResponseDTO> deleteOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

}
