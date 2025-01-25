package org.example.deliveryservice.service;



import org.example.deliveryservice.dto.orderDto.OrderCreateDto;
import org.example.deliveryservice.dto.orderDto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrderFromCart(Long cartId);
    OrderResponseDto getOrderById(Long orderId);
    List<OrderResponseDto> getAllOrders();
    void updateOrderStatus(Long orderId, String newStatus);
    void deleteOrderById(Long orderId);
    boolean saveToHistory(Long orderId);
    OrderResponseDto createOrderFromMultipleCarts(List<Long> cartIds);

    List<OrderResponseDto> getMyOrders();

    void softDeleteOrderById(Long orderId);
}