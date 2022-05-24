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
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.inject.Inject;

@SecuredResource
@Path("/preferences")
public class Preferences {
    private DatabasePoolService database;
    private String getPreferencesString = "SELECT * FROM preference WHERE user_id = ?;";

    private String setPreferencesString = "UPDATE preference " +
            "SET low_target_age = ?, high_target_age = ?, " +
            "within_x_miles = ?, preferred_gender = ? " +
            "WHERE user_id = ?;";

    @Inject
    public Preferences(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response getPreferences(@Context SecurityContext ctx) {
        PreferencesMessage prefs = new PreferencesMessage();

        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement getPreferencesStatement = conn.prepareStatement(getPreferencesString);
            getPreferencesStatement.setString(1, clientUserId);
            ResultSet resultSet = getPreferencesStatement.executeQuery();

            // user does not exist
            if(!resultSet.next()) {
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            prefs.lowerAgeLimit = resultSet.getInt("low_target_age");
            prefs.upperAgeLimit = resultSet.getInt("high_target_age");
            prefs.withinXMiles = resultSet.getInt("within_x_miles");
            prefs.preferredGender = resultSet.getString("preferred_gender");
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(prefs).build();
    }

    @PUT
    @Consumes("application/json")
    public Response setPreferences(@Context SecurityContext ctx, PreferencesMessage prefs) {
        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement setPreferencesStatement = conn.prepareStatement(setPreferencesString);
            setPreferencesStatement.setInt(1, prefs.lowerAgeLimit);
            setPreferencesStatement.setInt(2, prefs.upperAgeLimit);
            setPreferencesStatement.setInt(3, prefs.withinXMiles);
            setPreferencesStatement.setString(4, prefs.preferredGender);
            setPreferencesStatement.setString(5, clientUserId);
            setPreferencesStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}