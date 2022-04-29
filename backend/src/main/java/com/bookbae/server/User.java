package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;
import java.util.UUID;
import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;

@SecuredResource
@Path("/user")
public class User {
    
    private RestApplication application;

    @Inject
    public User(RestApplication application) {
        this.application = application;
    }

    @GET
    @Produces("application/json")
    public Response getUser(@Context SecurityContext ctx) {
        if(ctx == null) {
            // Shouldn't happen
            // TODO: log this condition
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

    @PUT
    @Produces("application/json")
    public Response putUser(@Context SecurityContext ctx, UserRequest req) {
        if(ctx == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserResponse resp = new UserResponse(req);
        try {
            Connection conn = this.application.getConnection();
            // Do the same as above but update the stuff
            // Return the updated version
            // Don't update the UUID that would be a bad bug
            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }
}