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
    private String getRecommendedUsersString = "SELECT * FROM user_info " +
            "WHERE user_id != ?";

    @Inject
    public Recommends(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response getRecommends(@Context SecurityContext ctx) {
        String clientUserId = ctx.getUserPrincipal().getName();
        ArrayList<UserResponse> entries = new ArrayList<>();
        UserResponse nextUserResponse;

        try (Connection conn = this.database.getConnection()) {

            PreparedStatement getUserPreferencesStatement = conn.prepareStatement(getUserPreferencesString);
            getUserPreferencesStatement.setString(1, clientUserId);
            ResultSet resultSet = getUserPreferencesStatement.executeQuery();
            resultSet.next(); // assume user exists

            int lowerAge = resultSet.getInt("low_target_age");
            int upperAge = resultSet.getInt("high_target_age");
            int withinXMiles = resultSet.getInt("within_x_miles");
            String[] preferredGenders = resultSet.getString("preferred_gender").split("_");

            PreparedStatement getRecommendedUsersStatement = conn.prepareStatement(getRecommendedUsersString);
            getRecommendedUsersStatement.setString(1, clientUserId);
            resultSet = getRecommendedUsersStatement.executeQuery();

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