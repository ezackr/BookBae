package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;

@SecuredResource
@Path("/chats")
public class Chats {
    private DatabasePoolService database;
    
    @Inject
    public Chats(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response getAllChats(@Context SecurityContext ctx) {
        try (Connection conn = this.database.getConnection()) {
            //sql
            // return all chat IDs
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @Path("/{chatId}")
    @GET
    @Produces("application/json")
    public Response getChat(@Context SecurityContext ctx, @PathParam("likeId") String likeId) {
        // be sure to check that the user
        try (Connection conn = this.database.getConnection()) {
            //sql
            // return all chat IDs
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}