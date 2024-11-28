package org.example.deliveryservice.dto.orderItemDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}