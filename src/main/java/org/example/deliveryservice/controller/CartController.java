package org.example.deliveryservice.controller;

import jakarta.validation.Valid;
import org.example.deliveryservice.dto.cartDto.CartCreateDto;
import org.example.deliveryservice.dto.cartDto.CartResponseDto;
import org.example.deliveryservice.dto.cartDto.CartUpdateDto;
import org.example.deliveryservice.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(@Valid @RequestBody CartCreateDto cartCreateDto) {
        CartResponseDto cart = cartService.createCart(cartCreateDto);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<CartResponseDto> updateCart(@Valid @RequestBody CartUpdateDto cartUpdateDto) {
        CartResponseDto cart = cartService.updateCart(cartUpdateDto);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/update-my-cart")
    ResponseEntity<CartResponseDto> updateMyCart(@Valid @RequestBody CartUpdateDto cartUpdateDto) {
        CartResponseDto cart = cartService.updateMyCart(cartUpdateDto);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDto>> getCartByUserId(@PathVariable Long userId) {
        List<CartResponseDto> carts = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }

    @GetMapping("/my-carts")
    public ResponseEntity<List<CartResponseDto>> createMyCarts() {
        List<CartResponseDto> carts = cartService.getMyCarts();
        return new ResponseEntity<>(carts, HttpStatus.OK);
    }


    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCartById(@PathVariable Long cartId) {
        cartService.deleteCartById(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete-my-cart/{cartId}")
    public ResponseEntity<Void> deleteMyCart(@PathVariable Long cartId) {
        cartService.deleteMyCart(cartId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
