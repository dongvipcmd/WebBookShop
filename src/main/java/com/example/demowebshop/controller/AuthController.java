package com.example.demowebshop.controller;

import com.example.demowebshop.entity.User;
import com.example.demowebshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {

        if(userRepository.existsByUsername(user.getUsername())){
            model.addAttribute("error", "User đã tồn tại vui lòng nhập tên khác");
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công!");

        return "redirect:/login";
    }

    @GetMapping("/redirect")
    public String redirect(Authentication authentication) {

        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return "redirect:/admin";
            }
        }

        return "redirect:/home";
    }
}
