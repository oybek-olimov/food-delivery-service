package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.Order;
import org.example.deliveryservice.entity.auth.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUserAndDeletedIsFalse(AuthUser user);

    List<Order> findAllByDeletedIsFalse();

    Optional<Order> findByOrderIdAndDeletedFalse(Long orderId);
}
