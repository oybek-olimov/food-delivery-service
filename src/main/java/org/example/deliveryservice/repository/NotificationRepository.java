package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.Notification;
import org.example.deliveryservice.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);

    List<Notification> findByUserIdAndIsRead(Long userId, boolean isRead);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByIsRead(boolean isRead);

    List<Notification> findByUserIdAndType(Long userId, NotificationType type);
}
