package org.example.deliveryservice.dto.notification;



import org.example.deliveryservice.dto.authUserDto.AuthUserDto;
import org.example.deliveryservice.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        String message,
        boolean isRead,
        NotificationType type,
        LocalDateTime sentAt,
        AuthUserDto user) {
}
