package com.example.demowebshop.controller;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demowebshop.config.CustomUserDetails;
import com.example.demowebshop.entity.Order;
import com.example.demowebshop.entity.User;
import com.example.demowebshop.service.OrderService;
import com.example.demowebshop.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    
    UserService userService;
    OrderService orderService;


    // HIỂN THỊ TRANG CÁ NHÂN
    @GetMapping("/profile")
    public String showProfile(Authentication auth, Model model, HttpSession session) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Optional<User> user = userService.findById(userDetails.getId());
        Long userId = userDetails.getId();
        
        if (user.isPresent()) {
            model.addAttribute("user", user.get());

            var cart = orderService.getOrCreateOrder(userId, session);
            model.addAttribute("cartItems", orderService.getOrderItems(cart.getId()));

            return "user/profile";
        }
        return "redirect:/home";
    }

    // CẬP NHẬT THÔNG TIN
    @PostMapping("/update-info/{id}")
    public String updateUserInfo(@PathVariable("id") Long id, 
                                 @ModelAttribute User updatedUser,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.updateUserInfo(id, updatedUser);
        if (user != null) {
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật thông tin. Vui lòng thử lại!");
        }
        return "redirect:/user/profile";
    }

     @GetMapping("/change-password")
    public String showChangePassword(Authentication auth, Model model, HttpSession session) {
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/login";
        }

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        Optional<User> user = userService.findById(userDetails.getId());
        Long userId = userDetails.getId();
        
        if (user.isPresent()) {
            model.addAttribute("user", user.get());

            var cart = orderService.getOrCreateOrder(userId, session);
            model.addAttribute("cartItems", orderService.getOrderItems(cart.getId()));

            return "user/change-password";
        }
        return "redirect:/home";
    }

    // ĐỔI MẬT KHẨU
    @PostMapping("/change-password/{id}")
    public String changePassword(@PathVariable("id") Long id,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.changePassword(id, oldPassword, newPassword, confirmPassword);
        if (user != null) {
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không chính xác hoặc mật khẩu mới không khớp!");
        }
        return "redirect:/user/change-password";
    }
}