package com.bookbae.server.service;

import com.bookbae.server.SecretKeyService;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SecretKeyServiceImpl implements SecretKeyService {
    private byte[] keyBytes;

    public SecretKeyServiceImpl() {
        keyBytes = new byte[64];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(keyBytes);
    }

    @Override
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }
}