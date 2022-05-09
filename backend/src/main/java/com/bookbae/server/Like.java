package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.LikeRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import jakarta.inject.Inject;

@SecuredResource
@Path("/like")
public class Like {
    private DatabasePoolService database;
    public Like(DatabasePoolService database) {
        this.database = database;
    }

    @PUT
    @Produces("application/json")
    public Response doLike(@Context SecurityContext ctx, LikeRequest info) {
        try (Connection conn = this.database.getConnection()) {
            //sql
            //if mutual, onMutualLike
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    private void onMutualLike(UUID user1, UUID user2) {
        //make chat
        return;
    }
}