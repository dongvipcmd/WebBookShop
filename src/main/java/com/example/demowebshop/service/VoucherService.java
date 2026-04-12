package com.example.demowebshop.service;

import com.example.demowebshop.entity.Voucher;
import com.example.demowebshop.enums.VoucherType;
import com.example.demowebshop.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;

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
}
