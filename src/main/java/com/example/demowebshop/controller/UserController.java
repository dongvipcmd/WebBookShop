package com.example.demowebshop.controller;

import com.example.demowebshop.config.CustomUserDetails;
import com.example.demowebshop.entity.User;
import com.example.demowebshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private Long getUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return null;

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }

    @GetMapping
    public String viewProfile(Authentication auth, Model model) {
        Long userId = getUserId(auth);
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userService.getUserById(userId);
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user, Authentication auth, RedirectAttributes redirectAttributes) {
        Long userId = getUserId(auth);
        if (userId == null) return "redirect:/login";

        try {
            userService.updateProfile(userId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(Authentication auth) {
        Long userId = getUserId(auth);
        if (userId == null) return "redirect:/login";
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {
        Long userId = getUserId(auth);
        if (userId == null) return "redirect:/login";

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xác nhận mật khẩu không khớp!");
            return "redirect:/profile/change-password";
        }

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/profile/change-password";
        }

        return "redirect:/profile";
    }
}
