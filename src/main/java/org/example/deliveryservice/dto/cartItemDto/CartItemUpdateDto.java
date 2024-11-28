package org.example.deliveryservice.dto.cartItemDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    private Long cartItemId;
    @Positive(message = "Quantity must be greater than zero")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Max(value = 100, message = "Quantity cannot exceed 100")
    private Integer quantity;
}