package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import com.bookbae.server.json.AccountCredentials;


@Path("/login")
public class Login {
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response tryLogin(AccountCredentials data) {
        if(1 == 1) { //TODO: check username/password stored in data
            String jws = Jwts.builder().setSubject(data.getUsername()).signWith(Login.key).compact();
            return Response.ok(jws).build();
        } else {
            return Response.status(403).build();
        }
    }
}