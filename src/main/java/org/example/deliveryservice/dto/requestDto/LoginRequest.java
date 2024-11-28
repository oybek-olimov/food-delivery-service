package org.example.deliveryservice.dto.requestDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;


public record LoginRequest(
    @Email
    String email,
    @Column(nullable = false)
    @NotBlank
    String password) implements Serializable{
}