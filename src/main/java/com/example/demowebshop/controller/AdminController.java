package com.example.demowebshop.controller;

import com.example.demowebshop.repository.BookRepository;
import com.example.demowebshop.repository.OrderRepository;
import com.example.demowebshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

import com.example.demowebshop.dto.RevenueDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/admin")
    public String adminPage(Model model) {
        
        long totalBooks = bookRepository.count();
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.countByStatus("PAID");
        
        BigDecimal totalRevenue = orderRepository.sumTotalRevenue("PAID")
                .orElse(BigDecimal.ZERO);
                
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        
        // model.addAttribute("monthlyRevenue", orderRepository.getMonthlyRevenue());

        return "admin";
    }

    @GetMapping("/api/admin/revenue-chart")
    @ResponseBody
    public Map<String, Object> getChartData(
            @RequestParam("type") String type, // "day", "month", "year"
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDateTime start = startDate.atStartOfDay(); // Bắt đầu từ 00:00:00
        LocalDateTime end = endDate.atTime(23, 59, 59); // Đến 23:59:59

        List<RevenueDTO> chartData;

        if ("day".equalsIgnoreCase(type)) {
            chartData = orderRepository.findRevenueByDay(start, end);
        } else if ("year".equalsIgnoreCase(type)) {
            chartData = orderRepository.findRevenueByYear(start, end);
        } else {
            chartData = orderRepository.findRevenueByMonth(start, end); // Mặc định là tháng
        }

        // Tính tổng doanh thu trong khoảng thời gian đã chọn
        BigDecimal filteredTotal = chartData.stream()
                .map(RevenueDTO::getRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("chartData", chartData);
        response.put("filteredTotal", filteredTotal);

        return response;
    }
}
