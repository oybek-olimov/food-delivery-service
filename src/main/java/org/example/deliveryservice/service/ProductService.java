package org.example.deliveryservice.service;


import org.example.deliveryservice.dto.productDto.CreateProductDto;
import org.example.deliveryservice.dto.productDto.ProductResponseDto;
import org.example.deliveryservice.dto.productDto.UpdateDiscountProductDto;
import org.example.deliveryservice.dto.productDto.UpdateIsPresentProductDto;
import org.example.deliveryservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductResponseDto createProduct(CreateProductDto product);
    List<ProductResponseDto> getAllProducts();
    Optional<Product> getProductById(Long id);
    Product updateProduct(CreateProductDto dto, Product product);
    Product updateProductIsPresent(UpdateIsPresentProductDto dto, Product product);
    Product updateProductDiscount(UpdateDiscountProductDto dto, Product product);
    void deleteProductById(Long id);
    List<ProductResponseDto> getProductsByCategoryName(String categoryName);
    List<ProductResponseDto> getProductsByPopularity();
    List<ProductResponseDto> searchByName(String name);
}
