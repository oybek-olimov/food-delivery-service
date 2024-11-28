package org.example.deliveryservice.service;


import org.example.deliveryservice.dto.categoryDto.CategoryCreateDTO;
import org.example.deliveryservice.dto.categoryDto.CategoryResponseDTO;
import org.example.deliveryservice.dto.categoryDto.CategoryUpdateDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO);

    CategoryResponseDTO getCategoryById(Long id);

    List<CategoryResponseDTO> getAllCategories();

    CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO);

    void deleteCategory(Long id);

    CategoryResponseDTO getCategoryByName(String name);
}
