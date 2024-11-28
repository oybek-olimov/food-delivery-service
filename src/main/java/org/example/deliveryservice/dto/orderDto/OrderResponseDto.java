package org.example.deliveryservice.dto.orderDto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.deliveryservice.dto.orderItemDto.OrderItemDto;
import org.example.deliveryservice.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String email;
    private LocalDate orderDate;
    private List<OrderItemDto> orderItems;
    private double totalAmount;
    private OrderStatus orderStatus;

}