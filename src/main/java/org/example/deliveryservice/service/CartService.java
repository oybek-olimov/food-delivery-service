package org.example.deliveryservice.service;



import org.example.deliveryservice.dto.cartDto.CartCreateDto;
import org.example.deliveryservice.dto.cartDto.CartResponseDto;
import org.example.deliveryservice.dto.cartDto.CartUpdateDto;

import java.util.List;

public interface CartService {
    CartResponseDto createCart(CartCreateDto cartCreateDto);

    List<CartResponseDto> getCartByUserId(Long userId);

    CartResponseDto updateCart(CartUpdateDto cartUpdateDto);

    void deleteCartById(Long cartId);

    List<CartResponseDto> getMyCarts();

    void deleteMyCart(Long cartId);

    CartResponseDto updateMyCart(CartUpdateDto cartUpdateDto);
}