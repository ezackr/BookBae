package com.bookbae.server.security;

import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Provides an implementation of {@link jakarta.ws.rs.core.SecurityContext SecurityContext}
 * for {@link com.bookbae.server.security.JWTSecurityFilter JWTSecurityFilter} to construct.
 */
public class JWTSecurityContext implements SecurityContext {
    public static String BEARER_AUTH = "Bearer";
    private boolean isSecure;
    private Principal principal;

    public JWTSecurityContext(SecurityContext oldContext, Principal principal) {
        this.isSecure = oldContext.isSecure();
        this.principal = principal;
    }

    public String getAuthenticationScheme() {
        return JWTSecurityContext.BEARER_AUTH;
    }

    public Principal getUserPrincipal() {
        return this.principal;
    }

    public boolean isSecure() {
        return this.isSecure;
    }

    public boolean isUserInRole(String role) {
        return role.equals("user");
    }
}