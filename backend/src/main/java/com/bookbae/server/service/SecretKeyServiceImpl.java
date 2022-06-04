package com.bookbae.server.service;

import com.bookbae.server.SecretKeyService;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Provides an implementation of {@link com.bookbae.server.SecretKeyService SecretKeyService}.
 * This implementation is {@link jakarta.enterprise.context.ApplicationScoped ApplicationScoped},
 * so there is only one instance that is managed by the framework that will be shared by all
 * root resource classes and providers that require it.
 */
@ApplicationScoped
public class SecretKeyServiceImpl implements SecretKeyService {
    private byte[] keyBytes;

    public SecretKeyServiceImpl() {
        keyBytes = new byte[64];
        SecureRandom rand = new SecureRandom();
        rand.nextBytes(keyBytes);
    }

    /**
     * Gets a key
     * @return a {@link javax.crypto.SecretKey SecretKey} that represents this object's secret key
     */
    @Override
    public SecretKey getKey() {
        return Keys.hmacShaKeyFor(keyBytes);
    }
}