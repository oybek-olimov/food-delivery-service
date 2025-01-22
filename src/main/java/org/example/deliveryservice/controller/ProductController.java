package org.example.deliveryservice.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.deliveryservice.dto.productDto.CreateProductDto;
import org.example.deliveryservice.dto.productDto.ProductResponseDto;
import org.example.deliveryservice.dto.productDto.UpdateDiscountProductDto;
import org.example.deliveryservice.dto.productDto.UpdateIsPresentProductDto;
import org.example.deliveryservice.entity.Product;
import org.example.deliveryservice.exception.ResourceNotFoundException;
import org.example.deliveryservice.mapper.CategoryMapper;
import org.example.deliveryservice.mapper.IngredientMapper;
import org.example.deliveryservice.mapper.ProductMapper;
import org.example.deliveryservice.repository.ProductRepository;
import org.example.deliveryservice.service.CategoryService;
import org.example.deliveryservice.service.ProductService;
import org.example.deliveryservice.service.impl.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Product API", description = "API for product")
@RequestMapping("api/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Transactional
    public ResponseEntity<ProductResponseDto> addProduct(@Valid @RequestBody CreateProductDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @GetMapping("/search")
    public List<ProductResponseDto> searchProducts(@RequestParam String name) {
        return productService.searchByName(name);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{categoryName}/products")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(productService.getProductsByCategoryName(categoryName));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody CreateProductDto dto) {
        return ResponseEntity.ok(productService.updateProduct(dto, id));
    }

    @PutMapping("/{id}/isPresent")
    public ResponseEntity<ProductResponseDto> updateProductIsPresent(
            @PathVariable Long id, @RequestBody UpdateIsPresentProductDto dto) {

        return ResponseEntity.ok(productService.updateProductIsPresent(dto, id));
    }

    @PutMapping("/{id}/discount")
    public ResponseEntity<ProductResponseDto> updateProductDiscount(
            @PathVariable Long id, @RequestBody UpdateDiscountProductDto dto) {
        return ResponseEntity.ok(productService.updateProductDiscount(dto, id));
    }

    @GetMapping("/popularity")
    public ResponseEntity<List<ProductResponseDto>> getPopularProducts() {
        List<ProductResponseDto> popularProducts = productService.getProductsByPopularity();

        if (popularProducts != null && !popularProducts.isEmpty()) {
            return ResponseEntity.ok(popularProducts);
        }

        return ResponseEntity.notFound().build();
    }
}

