package org.example.deliveryservice.dto.notification;


import org.example.deliveryservice.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        String message,
        boolean isRead,
        NotificationType type,
        LocalDateTime sentAt,
        Long userId) {
}
