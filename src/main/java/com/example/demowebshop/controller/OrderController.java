package com.example.demowebshop.controller;

import com.example.demowebshop.config.CustomUserDetails;
import com.example.demowebshop.dto.CheckoutRequest;
import com.example.demowebshop.entity.Order;
import com.example.demowebshop.entity.User;
import com.example.demowebshop.repository.OrderDetailRepository;
import com.example.demowebshop.service.CategoryService;
import com.example.demowebshop.service.OrderService;
import com.example.demowebshop.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final VoucherService voucherService;
    private final OrderDetailRepository orderDetailRepository;

    // Lấy userId từ Authentication — null nếu khách chưa đăng nhập
    private Long getUserId(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) return null;

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }

    // GIỎ HÀNG
    @GetMapping("/cart")
    public String viewCart(Authentication auth, HttpSession session, Model model) {
        Long userId = getUserId(auth);
        Order cart = orderService.getOrCreateOrder(userId, session);

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", orderService.getOrderItems(cart.getId()));
        model.addAttribute("isLoggedIn", userId != null);
        return "order/cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication auth,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            orderService.addToCart(getUserId(auth), session, bookId, quantity);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam Long detailId,
                                 @RequestParam int quantity,
                                 Authentication auth,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            orderService.updateQuantity(getUserId(auth), session, detailId, quantity);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long detailId,
                                 Authentication auth,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteBookFromOrder(getUserId(auth), session, detailId);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cart";
    }
    // CHECKOUT
    @GetMapping("/checkout")
    public String checkoutPage(Authentication auth, HttpSession session, Model model) {
        Long userId = getUserId(auth);
        Order cart = orderService.getOrCreateOrder(userId, session);

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", orderService.getOrderItems(cart.getId()));
        model.addAttribute("request", new CheckoutRequest());
        model.addAttribute("isLoggedIn", userId != null);
        if (userId != null) {
            model.addAttribute("vouchers", voucherService.getAvailableVouchers(userId));
        }
        return "order/checkout";
    }

    // Áp voucher — chỉ user đăng nhập mới gọi được
    @PostMapping("/checkout/apply")
    public String applyVouchers(@ModelAttribute CheckoutRequest request,
                                Authentication auth,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Long userId = getUserId(auth);
        try {
            Order cart = orderService.applyVouchers(userId, session, request);
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", orderService.getOrderItems(cart.getId()));
            model.addAttribute("request", request);
            model.addAttribute("isLoggedIn", true);
            model.addAttribute("vouchers", voucherService.getAvailableVouchers(userId));
            return "order/checkout";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }

    //Xác nhận đặt hàng — cả khách lẫn user đều được
    @PostMapping("/checkout/place")
    public String placeOrder(Authentication auth,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.placeOrder(getUserId(auth), session);
            return "redirect:/payment/" + order.getId();
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }


    @GetMapping("/payment/{orderId}")
    public String paymentPage(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order/payment";
    }

    @PostMapping("/payment/{orderId}/confirm")
    public String confirmPayment(@PathVariable Long orderId,
                                 Authentication auth,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        try {
            orderService.confirmPayment(orderId, getUserId(auth), session);
            return "redirect:/order/success/" + orderId;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/payment/" + orderId;
        }
    }

    @GetMapping("/order/success/{orderId}")
    public String successPage(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order/success";
    }


    @GetMapping("/order/history")
    public String orderHistory(Authentication auth, Model model) {
        Long userId = getUserId(auth);
        if (userId == null) return "redirect:/login";
        model.addAttribute("orders", orderService.getOrderHistory(userId));
        return "order/history";
    }

    @GetMapping("/admin/statistical")
    public String dashboard(Model model) {

        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("bookSales", orderService.getBookSales());

        return "statical/statistical";
    }

}