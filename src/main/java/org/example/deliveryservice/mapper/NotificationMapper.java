package org.example.deliveryservice.mapper;

import org.example.deliveryservice.dto.notification.NotificationCreateDto;
import org.example.deliveryservice.dto.notification.NotificationDto;
import org.example.deliveryservice.dto.notification.NotificationResponseDto;
import org.example.deliveryservice.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuthUserMapper.class)
public interface NotificationMapper {

    @Mapping(source = "userId", target = "user.id")
    Notification fromCreateDto(NotificationCreateDto dto);

    NotificationDto toDto(Notification notification);

    NotificationResponseDto toResponseDto(Notification notification);
}
