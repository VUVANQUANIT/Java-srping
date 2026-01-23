package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.DTO.OrderRequestDTO;
import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.ENUM.OrderStatus;
import com.example.JavaSpring1.Entity.Order;
import com.example.JavaSpring1.Exception.ResourceNotFoundException;
import com.example.JavaSpring1.Repository.OrderRepository;
import com.example.JavaSpring1.mappers.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    /**
     * Lấy tất cả orders kèm theo user information
     */
    public List<OrderResponseDTO> getAllOrders() {
        log.debug("Fetching all orders with user information");
        List<Order> orders = orderRepository.findAllWithUser();
        return orderMapper.toResponseDTOList(orders);
    }

    /**
     * Lấy order theo ID kèm theo user information
     */
    public OrderResponseDTO getOrderById(Long id) {
        log.debug("Fetching order by id: {} with user information", id);
        Order order = orderRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponseDTO(order);
    }

    /**
     * Lấy order có amount lớn nhất (tất cả status)
     */
    public OrderResponseDTO getOrderMax() {
        log.debug("Fetching order with maximum amount");
        List<Order> orders = orderRepository.findAllOrderByAmountDesc();
        if (orders.isEmpty()) {
            return null;
        }
        return orderMapper.toResponseDTO(orders.get(0));
    }

    /**
     * Lấy order có amount lớn nhất với status PAID
     */
    public OrderResponseDTO getOrderMaxPaid() {
        log.debug("Fetching order with maximum amount and PAID status");
        List<Order> orders = orderRepository.findTopByStatusOrderByAmountDesc(OrderStatus.PAID);
        if (orders.isEmpty()) {
            return null;
        }
        return orderMapper.toResponseDTO(orders.get(0));
    }

    /**
     * Lấy orders có status PAID và amount >= minValue kèm theo user information
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrderMaxPaid(double minValue) {
        log.debug("Fetching orders with PAID status and amount >= {}", minValue);
        List<Order> orders = orderRepository.findByStatusAndAmountGreaterThanEqual(
                OrderStatus.PAID,
                minValue
        );
        return orderMapper.toResponseDTOList(orders);
    }

    /**
     * Lấy tất cả orders sắp xếp theo amount giảm dần kèm theo user information
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> sortByAmountDesc() {
        log.debug("Fetching all orders sorted by amount descending");
        List<Order> orders = orderRepository.findAllOrderByAmountDesc();
        return orderMapper.toResponseDTOList(orders);
    }

    /**
     * Đếm số lượng orders theo từng status
     */
    @Transactional(readOnly = true)
    public Map<OrderStatus, Long> countByStatus() {
        log.debug("Counting orders by status");
        List<Object[]> results = orderRepository.countByStatus();
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (OrderStatus) row[0],
                        row -> (Long) row[1]
                ));
    }

    /**
     * Tính tổng amount của tất cả orders có status PAID
     */
    @Transactional(readOnly = true)
    public double totalAmount() {
        log.debug("Calculating total amount of PAID orders");
        Double total = orderRepository.sumAmountByStatus(OrderStatus.PAID);
        return total != null ? total : 0.0;
    }

    /**
     * Lấy orders theo user ID kèm theo user information
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        log.debug("Fetching orders for user id: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        return orderMapper.toResponseDTOList(orders);
    }

    /**
     * Lấy orders theo user email kèm theo user information
     */
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersByUserEmail(String email) {
        log.debug("Fetching orders for user email: {}", email);
        List<Order> orders = orderRepository.findByUserEmail(email);
        return orderMapper.toResponseDTOList(orders);
    }

    /**
     * Tạo order mới
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Long userId) {
        log.debug("Creating new order for user id: {}", userId);
        Order order = orderMapper.toEntity(orderRequestDTO);
        // Note: User sẽ được set từ controller/service khác
        Order savedOrder = orderRepository.save(order);
        // Fetch lại với user để có đầy đủ thông tin
        return orderMapper.toResponseDTO(
                orderRepository.findByIdWithUser(savedOrder.getId())
                        .orElse(savedOrder)
        );
    }

    /**
     * Cập nhật order
     */
    @Transactional
    public OrderResponseDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        log.debug("Updating order id: {}", id);
        Order existingOrder = orderRepository.findByIdWithUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        // Update fields
        existingOrder.setCustomerName(orderRequestDTO.getCustomerName());
        existingOrder.setAmount(orderRequestDTO.getAmount());
        existingOrder.setStatus(orderRequestDTO.getStatus());
        
        Order updatedOrder = orderRepository.save(existingOrder);
        // Fetch lại với user
        return orderMapper.toResponseDTO(
                orderRepository.findByIdWithUser(updatedOrder.getId())
                        .orElse(updatedOrder)
        );
    }

    /**
     * Xóa order
     */
    @Transactional
    public void deleteOrder(Long id) {
        log.debug("Deleting order id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
