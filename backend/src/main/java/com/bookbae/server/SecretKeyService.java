package com.bookbae.server;

import javax.crypto.SecretKey;

/**
 * This interface defines the contract for secret keys. Secret keys are created by an implementation,
 * and used by root resource classes and providers that have access to an injected instance of this
 * interface.
 * @see jakarta.inject.Inject
 */ 
public interface SecretKeyService {
    /**
     * Allows a root resource class or provider to access the application's secret key
     * @return a {@link javax.crypto.SecretKey SecretKey} that represents the application's secret key
     */
    public SecretKey getKey();
}