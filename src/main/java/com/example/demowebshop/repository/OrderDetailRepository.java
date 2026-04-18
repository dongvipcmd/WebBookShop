package com.example.demowebshop.repository;

import com.example.demowebshop.entity.OrderDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);

    Optional<OrderDetail> findByOrderIdAndBookId(Long orderId, Long bookId);

    int countByOrderId(Long orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderDetail d WHERE d.orderId = ?1")
    void deleteByOrderId(Long orderId);
}
