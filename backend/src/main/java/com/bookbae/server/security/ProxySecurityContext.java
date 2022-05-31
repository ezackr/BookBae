package com.bookbae.server.security;

import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;

public class ProxySecurityContext implements SecurityContext {
    public static String BEARER_AUTH = "Bearer"; //Confusing
    private boolean isSecure;
    private Principal principal;
    private String authScheme;

    public ProxySecurityContext(SecurityContext oldContext, String userid) {
        this.isSecure = oldContext.isSecure();
        this.principal = new ProxyPrincipal(userid);
        this.authScheme = oldContext.getAuthenticationScheme();
    }

    public String getAuthenticationScheme() {
        return this.authScheme;
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

    private static class ProxyPrincipal implements Principal {
        public final String name;
        public ProxyPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public int hashCode() {
            return this.name.hashCode();
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean equals(Object other) {
            if(other != null && other instanceof ProxyPrincipal) {
                return ((ProxyPrincipal) other).name.equals(this.name);
            } else {
                return false;
            }
        }
    }
}