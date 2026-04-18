package com.example.demowebshop.repository;

import com.example.demowebshop.dto.RevenueDTO;
import com.example.demowebshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserIdAndStatus(Long userId, String status);

    List<Order> findByUserIdAndStatusNotOrderByOrderDateDesc(Long userId, String status);

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(@org.springframework.data.repository.query.Param("status") String status);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.finalAmount) FROM Order o WHERE o.status = :status")
    Optional<java.math.BigDecimal> sumTotalRevenue(@org.springframework.data.repository.query.Param("status") String status);

    @org.springframework.data.jpa.repository.Query("SELECT new com.example.demowebshop.dto.MonthlyRevenueDTO(MONTH(o.orderDate), SUM(o.finalAmount)) " +
           "FROM Order o " +
           "WHERE o.status = 'PAID' AND YEAR(o.orderDate) = YEAR(CURRENT_DATE) " +
           "GROUP BY MONTH(o.orderDate) " +
           "ORDER BY MONTH(o.orderDate)")
    List<com.example.demowebshop.dto.MonthlyRevenueDTO> getMonthlyRevenue();
    @Query("SELECT new com.example.demowebshop.dto.RevenueDTO(DAY(o.orderDate), MONTH(o.orderDate), YEAR(o.orderDate), SUM(o.finalAmount)) " +
           "FROM Order o WHERE o.status = 'PAID' AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
           "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate), DAY(o.orderDate) " +
           "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate), DAY(o.orderDate)")
    List<RevenueDTO> findRevenueByDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 2. Thống kê theo THÁNG
    @Query("SELECT new com.example.demowebshop.dto.RevenueDTO(MONTH(o.orderDate), YEAR(o.orderDate), SUM(o.finalAmount)) " +
           "FROM Order o WHERE o.status = 'PAID' AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
           "GROUP BY YEAR(o.orderDate), MONTH(o.orderDate) " +
           "ORDER BY YEAR(o.orderDate), MONTH(o.orderDate)")
    List<RevenueDTO> findRevenueByMonth(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 3. Thống kê theo NĂM
    @Query("SELECT new com.example.demowebshop.dto.RevenueDTO(YEAR(o.orderDate), SUM(o.finalAmount)) " +
           "FROM Order o WHERE o.status = 'PAID' AND o.orderDate >= :startDate AND o.orderDate <= :endDate " +
           "GROUP BY YEAR(o.orderDate) " +
           "ORDER BY YEAR(o.orderDate)")
    List<RevenueDTO> findRevenueByYear(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
