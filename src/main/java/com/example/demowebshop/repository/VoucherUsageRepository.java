package com.example.demowebshop.repository;

import com.example.demowebshop.entity.VoucherUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherUsageRepository extends JpaRepository<VoucherUsage, Long> {
    boolean existsByVoucherIdAndUserId(Long voucherId, Long userId);

    List<VoucherUsage> findByUserId(Long userId);

}
