package org.example.deliveryservice.service.impl;

import org.example.deliveryservice.dto.categoryDto.CategoryCreateDTO;
import org.example.deliveryservice.dto.categoryDto.CategoryResponseDTO;
import org.example.deliveryservice.dto.categoryDto.CategoryUpdateDTO;
import org.example.deliveryservice.entity.Category;
import org.example.deliveryservice.exception.ResourceNotFoundException;
import org.example.deliveryservice.repository.CategoryRepository;
import org.example.deliveryservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        Category category = new Category();
        category.setCategoryName(categoryCreateDTO.getCategoryName());
        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO categoryUpdateDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        category.setCategoryName(categoryUpdateDTO.getCategoryName());
        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO getCategoryByName(String name) {
        return categoryRepository.findByCategoryName(name)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with name: " + name));
    }

    private CategoryResponseDTO mapToDTO(Category category) {
        return new CategoryResponseDTO(category.getCategoryId(), category.getCategoryName());
    }
}