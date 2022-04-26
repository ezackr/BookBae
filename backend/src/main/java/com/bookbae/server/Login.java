package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.SQLException;
import com.bookbae.server.json.AccountCredentials;


@Path("/login")
public class Login {
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private RestApplication application;

    @Inject
    public Login(RestApplication application) {
        this.application = application;
    }
    
    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    public Response tryLogin(AccountCredentials data) {
        if(!data.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            Connection conn = this.application.getConnection();
            // Statement s = conn.getStatement();
            // select user_id hash salt from table
            // use data.passowrd() + salt to get tryHash
            // if they are equal, log in to the server
            //     -> String jws = Jwts.builder().setSubject(data.getUsername()).signWith(Login.key).compact();
            //        return Response.ok(jws).build();
            //TODO: return JSON object
            // else Response.status(Response.Status.FORBIDDEN).build()
            return Response.ok().build();
        } catch (SQLException e) {
            return Response.serverError().build();
        }

        
        
    }
}