package com.example.demowebshop.repository;

import com.example.demowebshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserIdAndStatus(Long userId, String status);

    List<Order> findByUserIdAndStatusNotOrderByOrderDateDesc(Long userId, String status);

    @Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.status = 'PAID'")
    BigDecimal getTotalRevenue();

}
