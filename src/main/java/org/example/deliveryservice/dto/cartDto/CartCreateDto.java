package org.example.deliveryservice.dto.cartDto;
import lombok.Data;
import org.example.deliveryservice.dto.cartItemDto.CartItemCreateDto;

import java.util.List;

@Data
public class CartCreateDto {
    private List<CartItemCreateDto> cartItems; // Savatchadagi har bir mahsulot uchun DTO
}