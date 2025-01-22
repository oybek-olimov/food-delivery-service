package org.example.deliveryservice.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.example.deliveryservice.entity.Otp;
import org.example.deliveryservice.repository.OtpRepository;
import org.example.deliveryservice.service.OtpService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OtpServiceImpl implements OtpService {

    private final JavaMailSender mailSender;

    private final OtpRepository otpRepository;

    public OtpServiceImpl(JavaMailSender mailSender, OtpRepository otpRepository) {
        this.mailSender = mailSender;
        this.otpRepository = otpRepository;
    }

    @Transactional
    public void sendOtp(String email) {
        String otpCode = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(5);

        Optional<Otp> existingOtp = otpRepository.findByEmail(email);
        if (existingOtp.isPresent()) {
            Otp otp = existingOtp.get();
            otp.setOtpCode(otpCode);
            otp.setExpirationTime(expirationTime);
            otpRepository.save(otp);
        } else {
            Otp otp = new Otp(email, otpCode, expirationTime);
            otpRepository.save(otp);
        }

        sendEmail(email, otpCode);
    }

    @Transactional
    public void sendEmail(String email, String otpCode) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = "<h1 style=\"color: red;\">Sizning OTP kodingiz: " + otpCode + "</h1>" +
                    "<p>Kod 5 daqiqa davomida amal qiladi.</p>";

            helper.setTo(email);
            helper.setSubject("OTP Kodingiz");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public boolean validateOTP(String email, String otpCode) {
        return otpRepository.findByEmail(email)
                .filter(otp -> otp.getOtpCode().equals(otpCode) && otp.getExpirationTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}

