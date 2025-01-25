package org.example.deliveryservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.configuration.SessionUser;
import org.example.deliveryservice.dto.orderDto.OrderCreateDto;
import org.example.deliveryservice.dto.orderDto.OrderResponseDto;
import org.example.deliveryservice.dto.orderItemDto.OrderItemDto;
import org.example.deliveryservice.entity.Cart;
import org.example.deliveryservice.entity.CartItem;
import org.example.deliveryservice.entity.Order;
import org.example.deliveryservice.entity.OrderHistory;
import org.example.deliveryservice.entity.OrderItem;
import org.example.deliveryservice.entity.Payment;
import org.example.deliveryservice.entity.Product;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.enums.OrderStatus;
import org.example.deliveryservice.mapper.OrderMapper;
import org.example.deliveryservice.repository.CartRepository;
import org.example.deliveryservice.repository.OrderHistoryRepository;
import org.example.deliveryservice.repository.OrderRepository;
import org.example.deliveryservice.repository.PaymentRepository;
import org.example.deliveryservice.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final SessionUser sessionUser;
    private final OrderHistoryRepository orderHistoryRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentRepository paymentRepository,
                            CartRepository cartRepository, SessionUser sessionUser, OrderHistoryRepository orderHistoryRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.sessionUser = sessionUser;
        this.orderHistoryRepository = orderHistoryRepository;
        this.orderMapper = orderMapper;
    }


    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> getMyOrders() {
        AuthUser user = sessionUser.getCurrentUser();
        List<Order> orders = orderRepository.findAllByUserAndDeletedIsFalse(user);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("Sizda hech qanday buyurtma mavjud emas.");
        }
        return orders.stream()
                .map(this::toOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public OrderResponseDto createOrderFromCart(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Savat topilmadi: ID = " + cartId));

        AuthUser user = cart.getAuthUser();
        if (user == null) {
            throw new IllegalStateException("Foydalanuvchi ma'lumotlari mavjud emas");
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Savat bo'sh: ID = " + cartId);
        }

        Payment payment = new Payment();
        payment.setPaymentMethod("Cash");
        paymentRepository.save(payment);

        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setPayment(payment);
        order.setOrderStatus(OrderStatus.PLACED.name());

        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> toOrderItem(cartItem, order))
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        Long totalAmount = orderItems.stream()
                .mapToLong(item -> item.getOrderedProductPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);


        orderRepository.save(order);

        log.info("Savatchadan buyurtma yaratildi: foydalanuvchi ID = {}, buyurtma ID = {}, umumiy miqdor = {}",
                user.getId(), order.getOrderId(), totalAmount);

        return toOrderResponseDto(order);
    }

    @Transactional
    @Override
    public OrderResponseDto createOrderFromMultipleCarts(List<Long> cartIds) {
        List<Cart> carts = cartRepository.findAllById(cartIds);
        if (carts.isEmpty()) {
            throw new IllegalArgumentException("Berilgan savatlar topilmadi: ID = " + cartIds);
        }

        AuthUser user = carts.get(0).getAuthUser();
        if (user == null || carts.stream().anyMatch(cart -> !cart.getAuthUser().equals(user))) {
            throw new IllegalStateException("Barcha savatlar bir xil foydalanuvchiga tegishli bo'lishi kerak.");
        }

        if (carts.stream().allMatch(cart -> cart.getCartItems().isEmpty())) {
            throw new IllegalStateException("Barcha savatlar bo'sh.");
        }

        Payment payment = new Payment();
        payment.setPaymentMethod("Cash");
        paymentRepository.save(payment);

        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setPayment(payment);
        order.setOrderStatus(OrderStatus.PLACED.name());

        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cart : carts) {
            List<OrderItem> itemsFromCart = cart.getCartItems().stream()
                    .map(cartItem -> toOrderItem(cartItem, order))
                    .toList();
            orderItems.addAll(itemsFromCart);
        }
        order.setOrderItems(orderItems);

        long totalAmount = orderItems.stream()
                .mapToLong(item -> item.getOrderedProductPrice() * item.getQuantity())
                .sum();
        order.setTotalAmount(totalAmount);

        orderRepository.save(order);

        cartRepository.deleteAll(carts);

        log.info("Bir nechta savatlardan buyurtma yaratildi va savatlar o'chirildi: foydalanuvchi ID = {}, buyurtma ID = {}, umumiy miqdor = {}",
                user.getId(), order.getOrderId(), totalAmount);

        return toOrderResponseDto(order);
    }


    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Buyurtma topilmadi: ID = " + orderId));
        return toOrderResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByDeletedIsFalse().stream()
                .map(this::toOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findByOrderIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Buyurtma topilmadi: ID = " + orderId));

        String currentUserRole = getCurrentUserRole();

        OrderStatus nextStatus;
        try {
            nextStatus = OrderStatus.valueOf(newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Noto'g'ri yangi status: " + newStatus);
        }

        validateStatusTransition(order.getOrderStatus(), nextStatus.name(), currentUserRole);

        String oldStatus = order.getOrderStatus();
        order.setOrderStatus(nextStatus.name());
        if (nextStatus.name().equals(OrderStatus.COMPLETED.name())) {
            saveToHistory(orderId);
            softDeleteOrderById(orderId);
        }
        orderRepository.save(order);

        log.info("Buyurtma holati o'zgartirildi: Order ID = {}, Oldingi holat = {}, Yangi holat = {}, O'zgartiruvchi foydalanuvchi = {}",
                orderId, oldStatus, newStatus, currentUserRole);
    }

    private String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Foydalanuvchi autentifikatsiya qilinmagan.");
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_USER"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Foydalanuvchi rolini aniqlashning imkoni bo'lmadi."));
    }

    private void validateStatusTransition(String currentStatus, String newStatus, String currentUserRole) {
        OrderStatus current = OrderStatus.valueOf(currentStatus);
        OrderStatus next = OrderStatus.valueOf(newStatus);

        switch (current) {
            case PLACED:
                if (next != OrderStatus.CONFIRMED) {
                    throw new IllegalStateException("Foydalanuvchi faqat PLACED holatidan CONFIRMED ga o'tkazishi mumkin.");
                }
                break;
            case CONFIRMED:
            case PREPARING:
            case READY_FOR_PICKUP:
            case OUT_FOR_DELIVERY:
                if (!currentUserRole.equals("ROLE_ADMIN")) {
                    throw new IllegalStateException("Bu holatlarni faqat admin o'zgartirishi mumkin.");
                }
                break;
            case DELIVERED:
                if (next != OrderStatus.COMPLETED) {
                    throw new IllegalStateException("Foydalanuvchi faqat DELIVERED holatidan COMPLETED ga o'tkazishi mumkin.");
                }
                break;
            default:
                throw new IllegalStateException("Noto'g'ri joriy status: " + currentStatus);
        }
    }


    @Override
    public void deleteOrderById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new IllegalArgumentException("Buyurtma topilmadi: ID = " + orderId);
        }
        orderRepository.deleteById(orderId);
        log.info("Buyurtma o'chirildi: ID = {}", orderId);
    }

    @Override
    public boolean saveToHistory(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Buyurtma topilmadi: ID = " + orderId));
        if (order.getOrderStatus().equals("COMPLETED")){
            OrderHistory history = orderMapper.orderToOrderHistory(order);
            orderHistoryRepository.save(history);
            return true;
        }
        return false;
    }


    private OrderItem toOrderItem(CartItem cartItem, Order order) {
        Product product = cartItem.getProduct();
        if (product == null) {
            throw new IllegalArgumentException("Mahsulot ma'lumotlari topilmadi: ID = " + cartItem.getProduct().getId());
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setDiscount(cartItem.getDiscount());
        orderItem.setOrderedProductPrice(cartItem.getProductPrice());
        return orderItem;
    }

    private OrderResponseDto toOrderResponseDto(Order order) {
        List<OrderItemDto> items = order.getOrderItems().stream()
                .map(item -> new OrderItemDto(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getQuantity(),
                        item.getDiscount(),
                        item.getOrderedProductPrice()))
                .collect(Collectors.toList());

        return new OrderResponseDto(
                order.getOrderId(),
                order.getEmail(),
                order.getOrderDate(),
                items,
                order.getTotalAmount(),
                OrderStatus.valueOf(order.getOrderStatus()));
    }




    @Transactional
    @Override
    public void softDeleteOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Buyurtma topilmadi: ID = " + orderId));

        if (order.getDeleted()) {
            throw new IllegalStateException("Buyurtma allaqachon o'chirilgan: ID = " + orderId);
        }

        order.setDeleted(true);
        orderRepository.save(order);

        log.info("Buyurtma soft delete qilindi: ID = {}", orderId);
    }


}
