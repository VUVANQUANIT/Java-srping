package com.example.JavaSpring1.Repository;

import com.example.JavaSpring1.Entity.Order;
import com.example.JavaSpring1.ENUM.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Lấy tất cả orders kèm theo user (JOIN FETCH để tránh N+1 problem)
     * Lưu ý: Trong JPQL phải dùng entity name (Order) chứ không phải table name (orders)
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user")
    List<Order> findAllWithUser();

    /**
     * Lấy order theo ID kèm theo user
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.id = :id")
    Optional<Order> findByIdWithUser(@Param("id") Long id);

    /**
     * Lấy order có amount lớn nhất (PAID status)
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.status = :status ORDER BY o.amount DESC")
    List<Order> findTopByStatusOrderByAmountDesc(@Param("status") OrderStatus status);

    /**
     * Lấy order có amount lớn nhất (tất cả status)
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user ORDER BY o.amount DESC")
    List<Order> findAllOrderByAmountDesc();

    /**
     * Lấy orders có status PAID và amount >= minAmount
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.status = :status AND o.amount >= :minAmount ORDER BY o.amount DESC")
    List<Order> findByStatusAndAmountGreaterThanEqual(
            @Param("status") OrderStatus status,
            @Param("minAmount") double minAmount
    );

    /**
     * Đếm số lượng orders theo status
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countByStatus();

    /**
     * Tính tổng amount của orders có status PAID
     */
    @Query("SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.status = :status")
    Double sumAmountByStatus(@Param("status") OrderStatus status);

    /**
     * Lấy orders theo user ID
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);

    /**
     * Lấy orders theo user email
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user WHERE o.user.email = :email")
    List<Order> findByUserEmail(@Param("email") String email);
}
