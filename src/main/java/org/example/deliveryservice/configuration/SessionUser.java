package org.example.deliveryservice.configuration;

import org.example.deliveryservice.entity.auth.AuthUser;
import org.example.deliveryservice.exception.UserNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SessionUser {

    public AuthUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("Foydalanuvchi autentifikatsiyadan o'tmagan!");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails currentUser) {
            return currentUser.getAuthUser();
        } else
            throw new UserNotAuthenticatedException("Foydalanuvchi ma'lumoti noto'g'ri!");
    }



}
