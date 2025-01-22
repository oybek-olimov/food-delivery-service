package org.example.deliveryservice.service;

public interface OtpService {

    void sendOtp(String email);

    void sendEmail(String email, String otpCode);
    boolean validateOTP(String email, String otpCode);
}
