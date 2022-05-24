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
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Objects;
import jakarta.inject.Inject;


@SecuredResource
@Path("/recommends")
public class Recommends {
    private DatabasePoolService database;
    private String getUserPreferencesString = "SELECT * FROM preference WHERE user_id = ?;";
    private String getRecommendedsString = "SELECT * FROM user_info " +
            "WHERE user_id != ? " +
            "AND DATEDIFF(year, birthday, GETDATE()) >= ? " +
            "AND DATEDIFF(year, birthday, GETDATE()) <= ? " +
            "AND ";

    @Inject
    public Recommends(DatabasePoolService database) {
        this.database = database;

        // GETDATE is Microsoft's syntax, CURRENT_TIMESTAMP is H2's syntax
        if (database.isMockDatabase()) {
            getRecommendedsString = getRecommendedsString.replace("GETDATE", "CURRENT_TIMESTAMP");
        }
    }

    @GET
    @Produces("application/json")
    public Response getRecommends(@Context SecurityContext ctx) {
        String clientUserId = ctx.getUserPrincipal().getName();
        ArrayList<UserResponse> entries = new ArrayList<>();
        UserResponse nextUserResponse;

        try (Connection conn = this.database.getConnection()) {

            // get user's preferences
            PreparedStatement getUserPreferencesStatement = conn.prepareStatement(getUserPreferencesString);
            getUserPreferencesStatement.setString(1, clientUserId);
            ResultSet resultSet = getUserPreferencesStatement.executeQuery();
            resultSet.next(); // assume user exists

            int lowerAge = resultSet.getInt("low_target_age");
            int upperAge = resultSet.getInt("high_target_age");
            // int withinXMiles = resultSet.getInt("within_x_miles");
            String[] preferredGenders = resultSet.getString("preferred_gender").split("_");

            // extend getRecommendsString to fit number of preferred genders
            StringBuffer getRecommendsBuffer = new StringBuffer(getRecommendedsString);
            getRecommendsBuffer.append("(");
            for(int i = 0; i < preferredGenders.length; i++) {
                getRecommendsBuffer.append(" gender = ? OR");
            }
            // remove last "OR", add ");"
            getRecommendsBuffer.delete(getRecommendsBuffer.length() - 3, getRecommendsBuffer.length());
            getRecommendsBuffer.append(");");

            // get recommendations for the user based on their preferences
            PreparedStatement getRecommendedsStatement = conn.prepareStatement(getRecommendsBuffer.toString());
            getRecommendedsStatement.setString(1, clientUserId);
            getRecommendedsStatement.setInt(2, lowerAge);
            getRecommendedsStatement.setInt(3, upperAge);
            for(int i = 0; i < preferredGenders.length; i++) {
                getRecommendedsStatement.setString(4 + i, preferredGenders[i]);
            }
            resultSet = getRecommendedsStatement.executeQuery();

            // load up user preferences into json response
            while(resultSet.next()) {
                nextUserResponse = new UserResponse();
                nextUserResponse.setUserId(resultSet.getString("user_id"));
                nextUserResponse.setName(resultSet.getString("name"));
                nextUserResponse.setGender(resultSet.getString("gender"));
                nextUserResponse.setFavGenre(resultSet.getString("fav_genre"));
                // saves birthday as a string if not null
                nextUserResponse.setBirthday(Objects.toString(resultSet.getDate("birthday")));
                nextUserResponse.setBio(resultSet.getString("bio"));
                entries.add(nextUserResponse);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(entries).build();
    }
}