package org.example.deliveryservice.mapper;

import org.example.deliveryservice.dto.productDto.CreateProductDto;
import org.example.deliveryservice.dto.productDto.ProductResponseDto;
import org.example.deliveryservice.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    Product toProduct(CreateProductDto dto);

    ProductResponseDto toProductResponseDto(Product product);


    ProductResponseDto toProductResponseDto(CreateProductDto product);

    List<ProductResponseDto> toProductResponseDtoList(List<Product> products);
}
