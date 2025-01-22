package org.example.deliveryservice.controller;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.example.deliveryservice.configuration.GoogleTokenValidator;
import org.example.deliveryservice.service.impl.AuthUserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    private final GoogleTokenValidator googleTokenValidator;
    private final AuthUserServiceImpl authUserServiceImpl;

    public OAuth2Controller(GoogleTokenValidator googleTokenValidator, AuthUserServiceImpl authUserServiceImpl) {
        this.googleTokenValidator = googleTokenValidator;
        this.authUserServiceImpl = authUserServiceImpl;
    }

    @PostMapping("/verify-token")
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token format");
        }

        String idTokenString = authHeader.substring(7);

        GoogleIdToken.Payload payload = googleTokenValidator.validateToken(idTokenString);

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", payload.getEmail());
        userInfo.put("name", (String) payload.get("name"));
        return ResponseEntity.ok(authUserServiceImpl.processUser(userInfo));

    }


}
