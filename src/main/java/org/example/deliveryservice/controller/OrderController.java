package org.example.deliveryservice.controller;

import org.example.deliveryservice.dto.orderDto.OrderCreateDto;
import org.example.deliveryservice.dto.orderDto.OrderResponseDto;
import org.example.deliveryservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        return ResponseEntity.ok(orderService.createOrder(orderCreateDto));
    }

    @PostMapping("/create-from-cart/{cartId}")
    public ResponseEntity<OrderResponseDto> createOrderFromCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(orderService.createOrderFromCart(cartId));
    }

    @GetMapping("/get-my-order")
    public ResponseEntity<List<OrderResponseDto>> getMyOrder() {
        return ResponseEntity.ok(orderService.getMyOrders());
    }

    @PostMapping("/create-from-many-carts/{cartIds}")
    public ResponseEntity<OrderResponseDto> createOrderFromManyCartIds(@PathVariable List<Long> cartIds) {
        return ResponseEntity.ok(orderService.createOrderFromMultipleCarts(cartIds));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId,
                                                    @RequestParam String newStatus) {
        orderService.updateOrderStatus(orderId, newStatus);
        return ResponseEntity.ok("Buyurtma statusi yangilandi.");
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/soft-delete/{orderId}")
    public ResponseEntity<String> softDeleteOrder(@PathVariable Long orderId) {
        orderService.softDeleteOrderById(orderId);
        return ResponseEntity.ok("Buyurtma soft delete qilindi.");
    }

}
