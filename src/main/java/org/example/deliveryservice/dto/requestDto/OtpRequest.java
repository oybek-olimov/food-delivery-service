package org.example.deliveryservice.dto.requestDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;



public class OtpRequest implements Serializable {
    @Email
    String email;
    @Column(nullable = false)
    @NotBlank
    String otpCode;
}