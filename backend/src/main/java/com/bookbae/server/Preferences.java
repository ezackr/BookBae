package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.PreferencesMessage;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;

@SecuredResource
@Path("/preferences")
public class Preferences {
    private DatabasePoolService database;

    @Inject
    public Preferences(DatabasePoolService database) {
        this.database = database;
    }

    @Path("/get")
    @GET
    @Produces("application/json")
    public Response getPreferences(@Context SecurityContext ctx) {
        PreferencesMessage prefs = new PreferencesMessage();
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok(prefs).build();
    }

    @Path("/set")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response setPreferences(@Context SecurityContext ctx, PreferencesMessage prefs) {
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok(prefs).build();
    }
}