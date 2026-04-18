package com.example.demowebshop.service;

import com.example.demowebshop.entity.Voucher;
import com.example.demowebshop.entity.VoucherUsage;
import com.example.demowebshop.enums.DiscountType;
import com.example.demowebshop.enums.VoucherType;
import com.example.demowebshop.repository.VoucherRepository;
import com.example.demowebshop.repository.VoucherUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;

    public void createVoucher(Voucher voucher){
        voucherRepository.save(voucher);
    }

    public Voucher getById(Long id){
       return voucherRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("voucher không tìm thấy"));
    }

    public void updateVoucher(Long id, Voucher newData){
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher không tìm thấy"));

        if (newData.getCode() != null)
            voucher.setCode(newData.getCode());

        if (newData.getVoucherType() != null)
            voucher.setVoucherType(newData.getVoucherType());

        if (newData.getMinOrderValue() != null)
            voucher.setMinOrderValue(newData.getMinOrderValue());

        if (newData.getTotalQuantity() != null)
            voucher.setTotalQuantity(newData.getTotalQuantity());

        if (newData.getStartDate() != null)
            voucher.setStartDate(newData.getStartDate());

        if (newData.getEndDate() != null)
            voucher.setEndDate(newData.getEndDate());

        if (newData.getActive() != null)
            voucher.setActive(newData.getActive());

        if (newData.getVoucherType() == VoucherType.DISCOUNT) {

            if (newData.getDiscountType() != null)
                voucher.setDiscountType(newData.getDiscountType());

            if (newData.getDiscountValue() != null)
                voucher.setDiscountValue(newData.getDiscountValue());

            if (newData.getDiscountPercent() != null)
                voucher.setDiscountPercent(newData.getDiscountPercent());

            if (newData.getMaxDiscount() != null)
                voucher.setMaxDiscount(newData.getMaxDiscount());

            //clear shipping
            voucher.setMaxShippingDiscount(null);

        } else if (newData.getVoucherType() == VoucherType.SHIPPING) {

            if (newData.getMaxShippingDiscount() != null)
                voucher.setMaxShippingDiscount(newData.getMaxShippingDiscount());

            //clear voucher
            voucher.setDiscountType(null);
            voucher.setDiscountValue(null);
            voucher.setDiscountPercent(null);
            voucher.setMaxDiscount(null);
        }

        voucherRepository.save(voucher);
    }


    public List<Voucher> getAll(){
        return voucherRepository.findAll();
    }

    public void deleteVoucher(Long id){
        voucherRepository.deleteById(id);
    }

    public Voucher validate(String code, Long userId, BigDecimal orderTotal, VoucherType expectedType) {
        Voucher v = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Mã voucher không tồn tại"));

        if (!Boolean.TRUE.equals(v.getActive()))
            throw new RuntimeException("Voucher đã bị vô hiệu hóa");

        LocalDate today = LocalDate.now();
        if (today.isBefore(v.getStartDate()) || today.isAfter(v.getEndDate()))
            throw new RuntimeException("Voucher đã hết hạn");

        if (v.getTotalQuantity() != null && v.getUsedQuantity() >= v.getTotalQuantity())
            throw new RuntimeException("Voucher đã hết lượt sử dụng");

        if (v.getMinOrderValue() != null && orderTotal.compareTo(v.getMinOrderValue()) < 0)
            throw new RuntimeException("Đơn hàng tối thiểu " + v.getMinOrderValue() + " ₫ mới dùng được mã này");

        if (v.getVoucherType() != expectedType)
            throw new RuntimeException("Mã này không phải voucher " +
                    (expectedType == VoucherType.DISCOUNT ? "giảm giá" : "giảm ship"));

        if (voucherUsageRepository.existsByVoucherIdAndUserId(v.getId(), userId))
            throw new RuntimeException("Bạn đã sử dụng voucher này rồi");

        return v;
    }

    public BigDecimal calcDiscount(Voucher v, BigDecimal orderTotal) {
        if (v.getVoucherType() != VoucherType.DISCOUNT) return BigDecimal.ZERO;

        BigDecimal discount;
        if (v.getDiscountType() == DiscountType.PERCENTAGE && v.getDiscountPercent() != null) {
            discount = orderTotal.multiply(v.getDiscountPercent())
                    .divide(BigDecimal.valueOf(100));
            if (v.getMaxDiscount() != null)
                discount = discount.min(v.getMaxDiscount());
        } else {
            discount = v.getDiscountValue() != null ? v.getDiscountValue() : BigDecimal.ZERO;
        }
        return discount.min(orderTotal);
    }

    public BigDecimal calcShippingDiscount(Voucher v, BigDecimal shippingFee) {
        if (v.getVoucherType() != VoucherType.SHIPPING) return BigDecimal.ZERO;
        return v.getMaxShippingDiscount() == null ? shippingFee : v.getMaxShippingDiscount().min(shippingFee);
    }

    // lấy ra các voucher khả dụng cho người dùng
    public List<Voucher> getAvailableVouchers(Long userId) {
        Set<Long> usedVoucherIds = voucherUsageRepository.findByUserId(userId)
                .stream()
                .map(VoucherUsage::getVoucherId)
                .collect(Collectors.toSet());

        return voucherRepository.findAll().stream()
                .filter(v -> !usedVoucherIds.contains(v.getId()))
                .collect(Collectors.toList());
    }

}
