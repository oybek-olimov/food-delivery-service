package org.example.deliveryservice.mapper;

import org.example.deliveryservice.dto.CategoryDto;
import org.example.deliveryservice.dto.categoryDto.CategoryResponseDTO;
import org.example.deliveryservice.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory2(CategoryDto dto);
    Category toCategory(CategoryResponseDTO dto);
}
