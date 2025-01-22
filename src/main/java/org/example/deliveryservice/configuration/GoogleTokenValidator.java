package org.example.deliveryservice.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenValidator {

    private static final String CLIENT_ID = "450004792370-md2ffot3dlh1nrtmf4diunif6h7881p7.apps.googleusercontent.com";

    @Transactional
    public GoogleIdToken.Payload validateToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new JacksonFactory()
            )
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            } else {
                throw new RuntimeException("Invalid ID token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Token verification failed: " + e.getMessage());
        }
    }
}
