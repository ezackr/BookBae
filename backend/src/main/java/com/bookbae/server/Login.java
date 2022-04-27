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
import com.bookbae.server.json.LoginRequest;
import com.bookbae.server.json.LoginResponse;


@Path("/login")
public class Login {
    private SecretKey key;
    private RestApplication application;

    @Inject
    public Login(RestApplication application) {
        this.application = application;
        this.key = application.getKey();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response tryLogin(LoginRequest data) {
        if(!data.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        try {
            Connection conn = this.application.getConnection();
            // Statement s = conn.getStatement();
            // select user_id hash salt from table using username (which is a uniqueidentifier)
            // use data.passowrd() + salt to generate hash
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        if(1==2) { //TODO: if generated hash is not equal to stored hash or if user doesn't exist
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        String jws = Jwts.builder().setSubject(data.getUsername())
                         .signWith(this.key).compact();
        return Response.ok(new LoginResponse(jws)).build();
    }
}