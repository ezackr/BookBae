package com.bookbae.server.security;

import jakarta.ws.rs.ext.Provider;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerRequestContext;
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

    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String authHeader = ctx.getHeaderString("Authorization");
        if(authHeader == null) {
            Response reauth = Response.status(401).build();
            ctx.abortWith(reauth);
            return;
        }
        if(authHeader.length() <= "Bearer ".length()) {
            Response forbidden = Response.status(403).build();
            ctx.abortWith(forbidden);
            return;
        }
        String authString = authHeader.substring("Bearer ".length());
        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(authString);
            SecurityContext newCtx = 
                new JWTSecurityContext(ctx.getSecurityContext(), new JWSBackedPrincipal(jws.getBody()));
            ctx.setSecurityContext(newCtx);
        } catch (ExpiredJwtException e) {
            Response reauth = Response.status(401).build();
            ctx.abortWith(reauth);
            return;
        } catch (JwtException e) {
            Response forbidden = Response.status(403).build();
            ctx.abortWith(forbidden);
            return;
        }
    }
}