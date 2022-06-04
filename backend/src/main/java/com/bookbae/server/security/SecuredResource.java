package com.bookbae.server.security;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import jakarta.ws.rs.NameBinding;

/**
 * Binds {@link com.bookbae.server.security.JWTSecurityFilter JWTSecurityFilter} to 
 * root resource classes that should be secured by that filter.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@NameBinding
public @interface SecuredResource {}