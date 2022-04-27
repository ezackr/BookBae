package com.bookbae.server.security;

import com.bookbae.server.RestApplication;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import java.io.IOException;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;
import java.security.Principal;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@SecuredResource
public class JWTSecurityFilter implements ContainerRequestFilter {

    @Inject
    private RestApplication application;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String authHeader = ctx.getHeaderString("Authorization");
        if(authHeader == null) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        if(authHeader.length() <= "Bearer ".length()) {
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }
        String authString = authHeader.substring("Bearer ".length());

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(application.getKey()).build().parseClaimsJws(authString);
            SecurityContext newCtx = 
                new JWTSecurityContext(ctx.getSecurityContext(), new JWSBackedPrincipal(jws.getBody()));
            ctx.setSecurityContext(newCtx);
        } catch (ExpiredJwtException e) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        } catch (JwtException e) {
            ctx.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            return;
        }
    }
}