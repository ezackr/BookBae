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

/**
 * Provides endpoints for getting and setting a user's preferences.
 *
 * <br>Click here for more details about what each endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@SecuredResource
@Path("/preferences")
public class Preferences {
    private DatabasePoolService database;
    private String GET_PREFERENCES = "SELECT * " +
            "FROM preference " +
            "WHERE user_id = ?;";

    private String SET_PREFERENCES = "UPDATE preference " +
            "SET low_target_age = ?, high_target_age = ?, " +
            "within_x_miles = ?, preferred_gender = ? " +
            "WHERE user_id = ?;";

    @Inject
    public Preferences(DatabasePoolService database) {
        this.database = database;
    }

    /**
     * Retreives the preferences of the client user
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @return If successful, returns a jakarta ResponseBuilder with an OK status containing a
     *            <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/PreferencesMessage.java">PreferencesMessage</a> object
     *            <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @GET
    @Produces("application/json")
    public Response getPreferences(@Context SecurityContext ctx) {
        PreferencesMessage prefs = new PreferencesMessage();

        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement getPreferencesStatement = conn.prepareStatement(GET_PREFERENCES);
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

    /**
     * Sets the preferences of the client user
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @param prefs A <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/PreferencesMessage.java">PreferencesMessage</a> object
     *              containing the desired preferences
     * @return If successful, returns a jakarta ResponseBuilder with an OK status.
     *         <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @PUT
    @Consumes("application/json")
    public Response setPreferences(@Context SecurityContext ctx, PreferencesMessage prefs) {
        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement setPreferencesStatement = conn.prepareStatement(SET_PREFERENCES);
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