package org.example.deliveryservice.dto.authUserDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


public record AuthUserDto(
    @Size(message = "First Name must be between 5 and 30 characters long", min = 5, max = 20)
    @Pattern(message = "First Name must not contain numbers or special characters", regexp = "^[a-zA-Z]*$")
    String name,
    @Email
    String email,
    @Column(nullable = false)
    @NotBlank
    String password) implements Serializable{
}