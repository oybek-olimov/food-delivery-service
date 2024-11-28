package org.example.deliveryservice.dto.cartDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.deliveryservice.dto.cartItemDto.CartItemResponseDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponseDto {
    private Long cartId;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
    private long totalPriceSom; // Umumiy narx foydalanuvchiga so'mda qaytariladi
}
