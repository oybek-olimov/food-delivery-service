package org.example.deliveryservice.configuration;


import org.example.deliveryservice.service.otp.OtpService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class OTPAuthenticationProvider implements AuthenticationProvider {

    private final OtpService otpService;

    public OTPAuthenticationProvider(OtpService otpService) {
        this.otpService = otpService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String otp = authentication.getCredentials().toString();

        if (otpService.validateOTP(email, otp)) {
            return new UsernamePasswordAuthenticationToken(email, otp, new ArrayList<>());
        } else {
            throw new BadCredentialsException("Noto'g'ri OTP kiritildi.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

