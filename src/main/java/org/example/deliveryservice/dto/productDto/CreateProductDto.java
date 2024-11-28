package org.example.deliveryservice.dto.productDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.example.deliveryservice.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDto {
    @NotBlank(message = "Product name must not be blank")
    @Size(min = 3, message = "Product name must contain at least 3 characters")
    @Schema(description = "product name" , example = "pizza")
    private String productName;

    @Positive(message = "Price must be a positive number")
    private Long price;

    private String image;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 6, message = "Product description must contain at least 6 characters")
    private String description;

    @NotEmpty(message = "Ingredients list must not be empty")
    private List<String> ingredients = new ArrayList<>();

    private CategoryDto category;
}
