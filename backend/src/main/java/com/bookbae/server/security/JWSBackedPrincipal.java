package com.bookbae.server.security;

import java.security.Principal;
import io.jsonwebtoken.Claims;

/**
 * Provides an implementation of {@link java.security.Principal Principal}
 * for {@link com.bookbae.server.security.JWTSecurityContext JWTSecurityContext} to construct.
 */
public class JWSBackedPrincipal implements Principal {
    String subject;
    public JWSBackedPrincipal(Claims claims) {
        this.subject = claims.getSubject();
    }

    public String getName() {
        return subject;
    }

    public int hashCode() {
        return subject.hashCode();
    }

    public String toString() {
        return "username: " + subject;
    }

    public boolean equals(Object obj) {
        if(obj instanceof JWSBackedPrincipal) {
            return ((JWSBackedPrincipal) obj).subject.equals(this.subject);
        } else {
            return false;
        }
    }
}