package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.GET;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;
import java.util.UUID;
import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.UserResponse;

@Path("/user")
public class User {
    
    private RestApplication application;

    @Inject
    public User(RestApplication application) {
        this.application = application;
    }

    @SecuredResource
    @GET
    @Produces("application/json")
    public Response getUser(@Context SecurityContext ctx) {
        if(ctx == null) {
            // Shouldn't happen
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserResponse resp = new UserResponse();
        try {
            Connection conn = this.application.getConnection();
            //ctx.getUserPrincipal().getName(); gets the UUID
            //Then use the UUID to look up an entry in the table?
            //Then populate the resp object with the values in this row?
            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        resp.setUserId(UUID.fromString(ctx.getUserPrincipal().getName()));
        return Response.ok(resp).build();
    }
}