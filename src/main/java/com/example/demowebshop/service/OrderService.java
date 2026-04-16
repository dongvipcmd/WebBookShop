package com.example.demowebshop.service;

import com.example.demowebshop.dto.CheckoutRequest;
import com.example.demowebshop.entity.*;
import com.example.demowebshop.enums.VoucherType;
import com.example.demowebshop.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final BigDecimal SHIPPING_FEE = BigDecimal.valueOf(30000);
    private static final String GUEST_ORDER_KEY = "guestId";

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;
    private final VoucherService voucherService;
    private final VoucherUsageRepository voucherUsageRepository;
    private final VoucherRepository voucherRepository;

   public Order getOrCreateOrder(Long userId, HttpSession session){

       if(userId != null){
           return orderRepository.findByUserIdAndStatus(userId, "PENDING")
                   .orElseGet(() -> createNewOrder(userId, session));
       }

       Long guestId = (Long) session.getAttribute(GUEST_ORDER_KEY);
       if(guestId != null){
           return orderRepository.findById(guestId)
                   .orElseGet(() -> createNewOrder(null, session));
       }
       return createNewOrder(null, session);
   }

   public Order createNewOrder(Long userId, HttpSession session){

       Order order = new Order();
       order.setUserId(userId);
       order.setStatus("PENDING");
       order.setTotalAmount(BigDecimal.ZERO);
       order.setDiscountAmount(BigDecimal.ZERO);
       order.setShippingFee(SHIPPING_FEE);
       order.setShippingDiscount(BigDecimal.ZERO);
       order.setFinalAmount(SHIPPING_FEE);

       orderRepository.save(order);

       // nếu là khách lưu orderId vào session để tìm lại lần sau
       if(userId == null){
           session.setAttribute(GUEST_ORDER_KEY, order.getId());
       }
       return order;
   }

    public List<OrderDetail> getOrderItems(Long orderId){
        List<OrderDetail> items = orderDetailRepository.findByOrderId(orderId);

        List<Long> bookIds = items.stream()
                .map(OrderDetail::getBookId)
                .toList();

        List<Book> books = bookRepository.findAllById(bookIds);

        // map bookId -> book
        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        // gắn vào item
        for (OrderDetail item : items) {
            item.setBook(bookMap.get(item.getBookId()));
        }

        return items;
    }
    @Transactional
    public void addToCart(Long userId, HttpSession session, Long bookId, int quantity) {

        Order cart = getOrCreateOrder(userId, session);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách"));

        Optional<OrderDetail> optionalDetail =
                orderDetailRepository.findByOrderIdAndBookId(cart.getId(), bookId);

        if (optionalDetail.isPresent()) {
            OrderDetail detail = optionalDetail.get();

            int newQuantity = detail.getQuantity() + quantity;
            detail.setQuantity(newQuantity);

            detail.setFinalPrice(
                    detail.getUnitPrice().multiply(BigDecimal.valueOf(newQuantity))
            );

            orderDetailRepository.save(detail);

        } else {
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(cart.getId());
            detail.setBookId(bookId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(book.getPrice());

            detail.setFinalPrice(
                    book.getPrice().multiply(BigDecimal.valueOf(quantity))
            );

            orderDetailRepository.save(detail);
        }
        recalcTotal(cart);
    }

    @Transactional
    public void updateQuantity(Long userId, HttpSession session, Long detailId, int quantity){
        Order cart = getOrCreateOrder(userId, session);
        OrderDetail detail = orderDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        if(quantity <=0 ){
            orderDetailRepository.delete(detail);
        }
        else {
            detail.setQuantity(quantity);
            detail.setFinalPrice(detail.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
            orderDetailRepository.save(detail);
        }
        recalcTotal(cart);
    }

    @Transactional
    public void deleteBookFromOrder(Long userId, HttpSession session, Long detailId){
        Order cart = getOrCreateOrder(userId, session);
        orderDetailRepository.deleteById(detailId);
        recalcTotal(cart);
    }

    @Transactional
    public Order applyVouchers(Long userId, HttpSession session, CheckoutRequest request) {
        if (userId == null)
            throw new RuntimeException("Vui lòng đăng nhập để sử dụng voucher");

        Order cart = getOrCreateOrder(userId, session);

        if (cart.getTotalAmount().compareTo(BigDecimal.ZERO) == 0)
            throw new RuntimeException("Giỏ hàng trống");

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal shippingDiscount = BigDecimal.ZERO;

        if (request.getDiscountCode() != null && !request.getDiscountCode().isBlank()) {
            Voucher dv = voucherService.validate(
                    request.getDiscountCode(), userId, cart.getTotalAmount(), VoucherType.DISCOUNT);
            discountAmount = voucherService.calcDiscount(dv, cart.getTotalAmount());
            cart.setDiscountVoucherId(dv.getId());
        } else {
            cart.setDiscountVoucherId(null);
        }

        if (request.getShippingCode() != null && !request.getShippingCode().isBlank()) {
            Voucher sv = voucherService.validate(
                    request.getShippingCode(), userId, cart.getTotalAmount(), VoucherType.SHIPPING);
            shippingDiscount = voucherService.calcShippingDiscount(sv, SHIPPING_FEE);
            cart.setShippingVoucherId(sv.getId());
        } else {
            cart.setShippingVoucherId(null);
        }

        cart.setDiscountAmount(discountAmount);
        cart.setShippingFee(SHIPPING_FEE);
        cart.setShippingDiscount(shippingDiscount);
        cart.setFinalAmount(cart.getTotalAmount() // tính tổng chi phí cuối
                .subtract(discountAmount)
                .add(SHIPPING_FEE)
                .subtract(shippingDiscount));

        return orderRepository.save(cart);
    }

    @Transactional
    public Order placeOrder(Long userId, HttpSession session){
        Order cart = getOrCreateOrder(userId, session);

        if(orderDetailRepository.countByOrderId(cart.getId()) == 0){
            throw new RuntimeException("Giỏ hàng trống");
        }
        //cart.setStatus("CONFIRMED");
        cart.setOrderDate(LocalDateTime.now());
        orderRepository.save(cart);

        return cart;
    }

    @Transactional
    public Order confirmPayment(Long orderId, Long userId, HttpSession session) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        if (!"PENDING".equals(order.getStatus()))
            throw new RuntimeException("Đơn hàng chưa được xác nhận");

        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);

        for(OrderDetail detail : details){
            Book book = bookRepository.findById(detail.getBookId())
                    .orElseThrow(()-> new RuntimeException("Book not found"));

            if(book.getStockQuantity() < detail.getQuantity()){
                throw new RuntimeException("Số lượng sách không đủ");
            }

            book.setStockQuantity(book.getStockQuantity() - detail.getQuantity());
            bookRepository.save(book);

        }

        if (order.getDiscountVoucherId() != null)
            saveUsage(order.getDiscountVoucherId(), userId, order.getId(), order.getDiscountAmount());
        if (order.getShippingVoucherId() != null)
            saveUsage(order.getShippingVoucherId(), userId, order.getId(), order.getShippingDiscount());

        order.setStatus("PAID");
        if (userId == null) session.removeAttribute(GUEST_ORDER_KEY);
        return orderRepository.save(order);
    }

    //lich su don hang cua user
    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUserIdAndStatusNotOrderByOrderDateDesc(userId, "PENDING");
    }

    // tính toán tổng tiền
    public void recalcTotal(Order cart){
       List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
       BigDecimal totalAmount = BigDecimal.ZERO; // tổng tiền chưa giảm gì cả

       for(OrderDetail detail : details){
           totalAmount = totalAmount.add(detail.getFinalPrice());
       }

        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal shippingDiscount = BigDecimal.ZERO;

        cart.setDiscountVoucherId(null);
        cart.setShippingVoucherId(null);

        BigDecimal shippingFee = SHIPPING_FEE;

        // thanh toán = tiền hàng - giảm giá + phí ship - giảm phí ship

        BigDecimal finalAmount = totalAmount
                .subtract(discountAmount)
                .add(shippingFee)
                .subtract(shippingDiscount);

        cart.setTotalAmount(totalAmount);
        cart.setDiscountAmount(discountAmount);
        cart.setShippingDiscount(shippingDiscount);
        cart.setFinalAmount(finalAmount);

        orderRepository.save(cart);
    }

    public void saveUsage(Long voucherId,Long userId, Long orderId, BigDecimal discountApplied) {
        VoucherUsage usage = new VoucherUsage();
        usage.setVoucherId(voucherId);
        usage.setUserId(userId);
        usage.setOrderId(orderId);
        usage.setUsedAt(LocalDateTime.now());

        usage.setDiscountApplied(
                discountApplied != null ? discountApplied : BigDecimal.ZERO
        );

        voucherUsageRepository.save(usage);

        // Cập nhật số lượng đã dùng của voucher
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));

        int currentUsed = voucher.getUsedQuantity() == null ? 0 : voucher.getUsedQuantity();

        voucher.setUsedQuantity(currentUsed + 1);

        voucherRepository.save(voucher);
    }
}
