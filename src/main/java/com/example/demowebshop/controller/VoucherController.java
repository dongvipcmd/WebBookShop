package com.example.demowebshop.controller;

import com.example.demowebshop.entity.Voucher;
import com.example.demowebshop.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping
    public String list(Model model){
        model.addAttribute("vouchers", voucherService.getAll());
        return "voucher/voucher-list";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("voucher", new Voucher());
        return "voucher/voucher-form";
    }

    @PostMapping("")
    public String create(@ModelAttribute Voucher voucher){
        voucherService.createVoucher(voucher);
        return "redirect:/vouchers";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model){
        model.addAttribute("voucher", voucherService.getById(id));
        return "voucher/voucher-form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Voucher voucher){
        voucherService.updateVoucher(id, voucher);
        return "redirect:/vouchers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        voucherService.deleteVoucher(id);
        return "redirect:/vouchers";
    }

}
