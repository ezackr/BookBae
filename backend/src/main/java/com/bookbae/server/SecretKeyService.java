package com.bookbae.server;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SecretKeyService {
    private byte[] keyBytes;

    public SecretKeyService() {
        keyBytes = new byte[64];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(keyBytes);
    }

    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }
}