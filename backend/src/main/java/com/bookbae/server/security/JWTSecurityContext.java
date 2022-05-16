package com.bookbae.server.security;

import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JWTSecurityContext implements SecurityContext {
    public static String BEARER_AUTH = "Bearer"; //Confusing
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