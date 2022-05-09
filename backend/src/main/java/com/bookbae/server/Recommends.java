package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.UserResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import jakarta.inject.Inject;

@SecuredResource
@Path("/recommends")
public class Recommends {
    private DatabasePoolService database;

    @Inject
    public Recommends(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response getRecommends(@Context SecurityContext ctx) {
        UserResponse entries[] = new UserResponse[20];
        try (Connection conn = this.database.getConnection()) {
            //sql
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entries).build();
    }   
}