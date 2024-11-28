package org.example.deliveryservice.dto.cartDto;

import lombok.Data;
import org.example.deliveryservice.dto.cartItemDto.CartItemUpdateDto;

import java.util.List;

@Data
public class CartUpdateDto {
    private Long cartId;
    private List<CartItemUpdateDto> cartItems;
}
