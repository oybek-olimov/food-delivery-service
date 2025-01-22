package org.example.deliveryservice.service;


import org.example.deliveryservice.dto.notification.NotificationCreateDto;
import org.example.deliveryservice.dto.notification.NotificationDto;
import org.example.deliveryservice.dto.notification.NotificationResponseDto;
import org.example.deliveryservice.dto.notification.NotificationUpdateDto;

import java.util.List;

public interface NotificationService {

    NotificationResponseDto create(NotificationCreateDto dto);

    NotificationDto get(Long id);

    List<NotificationDto> getAllByUserId(Long userId);

    List<NotificationDto> getAllByUserIdAndIsRead(Long userId, boolean isRead);

    NotificationDto update(Long id, NotificationUpdateDto dto);

    void delete(Long id);

    void sendEmailAndSaveNotification(String email, String message) throws RuntimeException;

}
