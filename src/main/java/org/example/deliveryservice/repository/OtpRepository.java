package org.example.deliveryservice.repository;

import org.example.deliveryservice.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findByEmail(String email);
/*

    @Query("select count(1) > 0 from Otp o where o.email = ?1 and o.otpCode = ?2 and o.expirationTime < ?3")
    boolean existsByCodeAndEmailNotExpired(String email, String otpCode, LocalDateTime dateTime);
*/

    void deleteByEmail(String email);
}