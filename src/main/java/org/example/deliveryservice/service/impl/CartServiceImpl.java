package org.example.deliveryservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.deliveryservice.configuration.SessionUser;
import org.example.deliveryservice.dto.cartDto.CartCreateDto;
import org.example.deliveryservice.dto.cartDto.CartResponseDto;
import org.example.deliveryservice.dto.cartDto.CartUpdateDto;
import org.example.deliveryservice.dto.cartItemDto.CartItemCreateDto;
import org.example.deliveryservice.dto.cartItemDto.CartItemResponseDto;
import org.example.deliveryservice.dto.cartItemDto.CartItemUpdateDto;
import org.example.deliveryservice.entity.Cart;
import org.example.deliveryservice.entity.CartItem;
import org.example.deliveryservice.entity.Product;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.repository.AuthUserRepository;
import org.example.deliveryservice.repository.CartRepository;
import org.example.deliveryservice.repository.ProductRepository;
import org.example.deliveryservice.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthUserRepository authUserRepository;
    private final SessionUser sessionUser;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, AuthUserRepository authUserRepository, SessionUser sessionUser) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.authUserRepository = authUserRepository;
        this.sessionUser = sessionUser;
    }

    @Transactional
    @Override
    public CartResponseDto createCart(CartCreateDto cartCreateDto) {
        AuthUser user = authUserRepository.findById(sessionUser.getCurrentUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Cart cart = new Cart();
        cart.setAuthUser(user);

        List<CartItem> cartItems = cartCreateDto.getCartItems().stream()
                .map(dto -> {
                    CartItem cartItem = toCartItem(dto);
                    cartItem.setCart(cart); // `Cart` ni `CartItem` bilan bog'lash
                    return cartItem;
                }).collect(Collectors.toList());
        cart.setCartItems(cartItems);

        cart.setTotalPrice(calculateTotalPrice(cartItems));

        cartRepository.save(cart);
        return toCartResponseDto(cart);
    }

    @Transactional
    @Override
    public CartResponseDto updateCart(CartUpdateDto cartUpdateDto) {
        Cart cart = cartRepository.findById(cartUpdateDto.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        for (CartItemUpdateDto updateDto : cartUpdateDto.getCartItems()) {
            Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                    .filter(item -> item.getCartItemId().equals(updateDto.getCartItemId()))
                    .findFirst();

            if (cartItemOpt.isPresent()) {
                CartItem cartItem = cartItemOpt.get();
                cartItem.setQuantity(updateDto.getQuantity());
                cartItem.setProductPrice(cartItem.getProduct().getPrice() - cartItem.getDiscount());
            }
        }

        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));
        cartRepository.save(cart);

        return toCartResponseDto(cart);
    }

    @Transactional
    @Override
    public CartResponseDto updateMyCart(CartUpdateDto cartUpdateDto) {
        AuthUser user = sessionUser.getCurrentUser();

        Cart cart = cartRepository.findById(cartUpdateDto.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("Savat topilmadi: ID = " + cartUpdateDto.getCartId()));

        if (!cart.getAuthUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Siz ushbu savatni yangilashga ruxsatga ega emassiz: ID = " + cartUpdateDto.getCartId());
        }

        for (CartItemUpdateDto updateDto : cartUpdateDto.getCartItems()) {
            Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                    .filter(item -> item.getCartItemId().equals(updateDto.getCartItemId()))
                    .findFirst();

            if (cartItemOpt.isPresent()) {
                CartItem cartItem = cartItemOpt.get();
                cartItem.setQuantity(updateDto.getQuantity());
                cartItem.setProductPrice(cartItem.getProduct().getPrice() - cartItem.getDiscount());
            } else {
                throw new IllegalArgumentException("Savat elementi topilmadi: ID = " + updateDto.getCartItemId());
            }
        }

        cart.setTotalPrice(calculateTotalPrice(cart.getCartItems()));

        cartRepository.save(cart);

        log.info("Foydalanuvchi ID = {} tomonidan savat yangilandi: Savat ID = {}", user.getId(), cartUpdateDto.getCartId());

        return toCartResponseDto(cart);
    }


    @Transactional
    @Override
    public List<CartResponseDto> getMyCarts() {
        AuthUser user = sessionUser.getCurrentUser();
        List<Cart> carts = cartRepository.findAllByAuthUserId(user.getId());
        return carts.stream()
                .map(this::toCartResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void deleteMyCart(Long cartId) {
        AuthUser user = sessionUser.getCurrentUser();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: ID = " + cartId));
        if (!cart.getAuthUser().getId().equals(user.getId())) {
            throw new IllegalStateException("Cart not found: ID = " + cartId);
        }
        cartRepository.delete(cart);

    }


    @Override
    public List<CartResponseDto> getCartByUserId(Long userId) {
        List<Cart> carts = cartRepository.findAllByAuthUserId(userId);

        if (carts.isEmpty()) {
            throw new IllegalArgumentException("No carts found for user");
        }

        return carts.stream()
                .map(this::toCartResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteCartById(Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
        } else {
            throw new IllegalArgumentException("Cart not found");
        }
    }

    private CartItem toCartItem(CartItemCreateDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.isPresent()) {
            throw new IllegalArgumentException("Product is not available: " + dto.getProductId());
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setDiscount(product.getDiscount());
        cartItem.setProductPrice(product.getPrice() - cartItem.getDiscount());
        return cartItem;
    }


    private long calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToLong(item -> (item.getProductPrice() * item.getQuantity()))
                .sum();
    }

    private CartResponseDto toCartResponseDto(Cart cart) {
        List<CartItemResponseDto> cartItems = cart.getCartItems().stream()
                .map(CartItemResponseDto::new)
                .collect(Collectors.toList());
        long totalPriceSom = cart.getTotalPrice();
        return new CartResponseDto(cart.getCartId(), cart.getAuthUser().getId(), cartItems, totalPriceSom);
    }
}