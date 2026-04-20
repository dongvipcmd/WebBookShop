package com.example.demowebshop.repository;

import com.example.demowebshop.dto.BookSalesDTO;
import com.example.demowebshop.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(Long orderId);

    Optional<OrderDetail> findByOrderIdAndBookId(Long orderId, Long bookId);

    int countByOrderId(Long orderId);


    @Query("""
    SELECT new com.example.demowebshop.dto.BookSalesDTO(
        b.name,
        SUM(od.quantity)
    )
    FROM OrderDetail od
    JOIN Book b ON od.bookId = b.id
    JOIN Order o ON od.orderId = o.id
    WHERE o.status = 'PAID'
    GROUP BY b.id, b.name
    ORDER BY SUM(od.quantity) DESC
""")
    List<BookSalesDTO> getBookSales();

}
