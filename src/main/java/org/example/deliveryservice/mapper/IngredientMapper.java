package org.example.deliveryservice.mapper;

import org.example.deliveryservice.dto.inredientDto.IngredientDto;
import org.example.deliveryservice.entity.Ingredients;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface IngredientMapper {
    Ingredients toIngredients(IngredientDto dto);
}
