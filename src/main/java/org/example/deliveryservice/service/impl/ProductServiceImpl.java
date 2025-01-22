package org.example.deliveryservice.service.impl;

import org.example.deliveryservice.dto.categoryDto.CategoryResponseDTO;
import org.example.deliveryservice.dto.productDto.CreateProductDto;
import org.example.deliveryservice.dto.productDto.ProductResponseDto;
import org.example.deliveryservice.dto.productDto.UpdateDiscountProductDto;
import org.example.deliveryservice.dto.productDto.UpdateIsPresentProductDto;
import org.example.deliveryservice.entity.Category;
import org.example.deliveryservice.entity.Product;
import org.example.deliveryservice.exception.ResourceNotFoundException;
import org.example.deliveryservice.mapper.CategoryMapper;
import org.example.deliveryservice.mapper.ProductMapper;
import org.example.deliveryservice.repository.CategoryRepository;
import org.example.deliveryservice.repository.OrderItemRepository;
import org.example.deliveryservice.repository.ProductRepository;
import org.example.deliveryservice.service.CategoryService;
import org.example.deliveryservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final CategoryService categoryService;
    private final OrderItemRepository orderItemRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, CategoryServiceImpl categoryService, ProductMapper productMapper, CategoryMapper categoryMapper, CategoryService categoryService1, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
        this.categoryService = categoryService1;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public ProductResponseDto createProduct(CreateProductDto dto) {
        CategoryResponseDTO category = categoryService.getCategoryByName(dto.getCategory().getName());
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        }
        Product product = productMapper.toProduct(dto);
        product.setCategory(categoryMapper.toCategory(category));
        productRepository.save(product);
        return productMapper.toProductResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public ProductResponseDto updateProduct(CreateProductDto dto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        Category category = categoryRepository.findByCategoryName(dto.getCategory().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + dto.getCategory().getName()));

        product.setProductName(dto.getProductName());
        product.setPrice(dto.getPrice());
        product.setImage(dto.getImage());
        product.setDescription(dto.getDescription());
        product.setIngredients(dto.getIngredients());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return productMapper.toProductResponseDto(updatedProduct);
    }

    @Override
    public ProductResponseDto updateProductIsPresent(UpdateIsPresentProductDto dto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (dto == null || product == null) {
            throw new IllegalArgumentException("Invalid input data");
        }
        product.setPresent(dto.isPresent());
        Product product1 = productRepository.save(product);
        return productMapper.toProductResponseDto(product1);
    }

    @Override
    public ProductResponseDto updateProductDiscount(UpdateDiscountProductDto dto, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        if (dto == null || product == null) {
            throw new IllegalArgumentException("Invalid input data");
        }
        if (product.getPrice() < dto.getDiscount()) {
            throw new IllegalArgumentException("Discount must be small than product price");
        }
        product.setDiscount(dto.getDiscount());
        Product product1 = productRepository.save(product);
        return productMapper.toProductResponseDto(product1);
    }


    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponseDto> getProductsByCategoryName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + categoryName));

        List<Product> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found for category: " + categoryName);
        }

        return productMapper.toProductResponseDtoList(products);
    }

    @Override
    public List<ProductResponseDto> getProductsByPopularity() {
        List<Object[]> popularityData = orderItemRepository.findProductsByPopularity();

        return popularityData.stream()
                .map(data -> {
                    Long productId = (Long) data[0];
                    Product product = productRepository.findById(productId).orElse(null);

                    if (product != null) {
                        ProductResponseDto productResponseDto = ProductResponseDto.builder()
                                .id(product.getId())
                                .productName(product.getProductName())
                                .price(product.getPrice())
                                .image(product.getImage())
                                .description(product.getDescription())
                                .ingredients(product.getIngredients())
                                .category(product.getCategory())
                                .isPresent(product.isPresent())
                                .discount(product.getDiscount())
                                .build();

                        return productResponseDto;
                    }
                    return null;
                })
                .filter(productResponseDto -> productResponseDto != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> searchByName(String name) {
        return productMapper.toProductResponseDtoList(productRepository.findByProductNameContainingIgnoreCase(name));
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productMapper.toProductResponseDto(product);
    }

}
