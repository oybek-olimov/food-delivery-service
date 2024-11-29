package org.example.deliveryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.deliveryservice.dto.notification.NotificationCreateDto;
import org.example.deliveryservice.dto.notification.NotificationDto;
import org.example.deliveryservice.dto.notification.NotificationUpdateDto;
import org.example.deliveryservice.enums.NotificationType;
import org.example.deliveryservice.repository.AuthUserRepository;
import org.example.deliveryservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final JavaMailSender mailSender;
    private final AuthUserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody @Valid NotificationCreateDto dto) {
        return ResponseEntity.ok(notificationService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.get(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDto>> getAllNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getAllByUserId(userId));
    }

    @GetMapping("/user/{userId}/status")
    public ResponseEntity<List<NotificationDto>> getNotificationsByUserIdAndStatus(
            @PathVariable Long userId,
            @RequestParam boolean isRead) {
        return ResponseEntity.ok(notificationService.getAllByUserIdAndIsRead(userId, isRead));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> updateNotification(@PathVariable Long id, @RequestBody @Valid NotificationUpdateDto dto) {
        return ResponseEntity.ok(notificationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String email, @RequestParam String message) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Yangi xabar");
            mailMessage.setText(message);
            mailSender.send(mailMessage);

            NotificationCreateDto notificationCreateDto = new NotificationCreateDto(
                    message,
                    NotificationType.INFO,
                    getUserIdByEmail(email)
            );
            notificationService.create(notificationCreateDto);

            return ResponseEntity.ok("Xabar email orqali yuborildi va bazaga saqlandi.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Xabar yuborishda xatolik yuz berdi: " + e.getMessage());
        }
    }

    private Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                .getId();
    }
}
