package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.ENUM.OrderStatus;
import com.example.JavaSpring1.Entity.Order;
import com.example.JavaSpring1.mappers.OrderMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    public static List<Order> DBODER = new ArrayList<>();
    private static Long idCounter = 1L;
    private final OrderMapper orderMapper;
    @PostConstruct
    public void initData() {
        if (DBODER.isEmpty()) {
            // Tạo 6+ orders như yêu cầu
            createOrder("Nguyen Van A", 1500000.0, OrderStatus.PAID);
            createOrder("Tran Thi B", 2500000.0, OrderStatus.NEW);
            createOrder("Le Van C", 800000.0, OrderStatus.DELIVERED);
            createOrder("Pham Thi D", 3200000.0, OrderStatus.PAID);
            createOrder("Hoang Van E", 1200000.0, OrderStatus.NEW);
            createOrder("Do Thi F", 500000.0, OrderStatus.PAID);
            createOrder("Nguyen Van G", 4500000.0, OrderStatus.FINISHED);
            createOrder("Tran Thi H", 1800000.0, OrderStatus.DELIVERED);

            System.out.println(" Đã tạo " + DBODER.size() + " đơn hàng mẫu");
        }
    }
    private void createOrder(String customer, double amount, OrderStatus status) {
        Order order = new Order();
        order.setId(idCounter++);
        order.setCustomerName(customer);
        order.setAmount(amount);
        order.setStatus(status);
        DBODER.add(order);
    }
    public List<OrderResponseDTO> getAllOrders() {
        return orderMapper.toResponseDTOList(DBODER);
    }
    public OrderResponseDTO getOrderById(Long id) {
        Order order = findOrderById(id);
        return orderMapper.toResponseDTO(order);
    }
    public OrderResponseDTO getOrderMax(){
        Optional<Order> maxOrder = findOrderMaxPaid();
        if(maxOrder.isPresent()){
            return orderMapper.toResponseDTO(maxOrder.get());
        }
        return null;
    }
    public List<OrderResponseDTO> getOrderMaxPaid(double minvalue) {
        return orderMapper.toResponseDTOList(getMaxListOrders(minvalue));
    }
    public List<OrderResponseDTO> sortByAmountDesc1(){
        return orderMapper.toResponseDTOList(sortByAmountDesc());
    }
    public Map<OrderStatus,Long> countByStatus(){
        return DBODER.stream().collect(Collectors.groupingBy(Order::getStatus, Collectors.counting()));
    }
    public List<Order> sortByAmountDesc() {
        return DBODER.stream()
                .sorted(Comparator.comparing(Order::getAmount)).toList();
    }
    public double totalAmount() {
        return DBODER.stream().filter(order -> order.getStatus() == OrderStatus.PAID).mapToDouble(Order::getAmount).sum();
    }
    public List<Order> getMaxListOrders(double minAmount) {
        return DBODER.stream().filter(order -> order.getStatus() == OrderStatus.PAID).filter(order -> order.getAmount()>minAmount).collect(Collectors.toList());
    }
    public Order findOrderById(Long id) {
        return  DBODER.stream().filter(order -> order.getId().equals(id)).findFirst().orElseThrow(()->new RuntimeException("Order not found"));
    }

    public Optional<Order> findOrderMaxPaid(){
        return DBODER.stream().max(Comparator.
                        comparing(Order::getAmount));
    }


}
