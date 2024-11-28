package org.example.deliveryservice.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.deliveryservice.enums.NotificationType;

public record NotificationCreateDto(
        @NotBlank(message = "Message cannot be blank")
        String message,

        @NotNull(message = "Notification type cannot be null")
        NotificationType type,

        @NotNull(message = "User ID cannot be null")
        Long userId) {
}
