package org.example.deliveryservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.deliveryservice.dto.notification.NotificationCreateDto;
import org.example.deliveryservice.dto.notification.NotificationDto;
import org.example.deliveryservice.dto.notification.NotificationResponseDto;
import org.example.deliveryservice.dto.notification.NotificationUpdateDto;
import org.example.deliveryservice.entity.Notification;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.mapper.NotificationMapper;
import org.example.deliveryservice.repository.AuthUserRepository;
import org.example.deliveryservice.repository.NotificationRepository;
import org.example.deliveryservice.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final AuthUserRepository userRepository;
    @Override
    public NotificationResponseDto create(NotificationCreateDto dto) {
        AuthUser user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("AuthUser not found with id: " + dto.userId()));

        Notification notification = mapper.fromCreateDto(dto);
        notification.setUser(user);

        notification = repository.save(notification);


        return mapper.toResponseDto(notification);
    }

    @Override
    public NotificationDto get(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return mapper.toDto(notification);
    }

    @Override
    public List<NotificationDto> getAllByUserId(Long userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getAllByUserIdAndIsRead(Long userId, boolean isRead) {
        return repository.findByUserIdAndIsRead(userId, isRead)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public NotificationDto update(Long id, NotificationUpdateDto dto) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setRead(dto.isRead());
        repository.save(notification);
        return mapper.toDto(notification);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
