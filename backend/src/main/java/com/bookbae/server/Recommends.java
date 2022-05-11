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
    // user_id = client user id; for now, return all users but self
    private String getRecommendedUsersString = "SELECT * FROM user_info WHERE user_id != ?;";

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
            PreparedStatement getRecommendedUsersStatement = conn.prepareStatement(getRecommendedUsersString);
            getRecommendedUsersStatement.setString(1, clientUserId);
            ResultSet resultSet = getRecommendedUsersStatement.executeQuery();

            // no users to recommend!
            if (!resultSet.next()) {
                //TODO: what to do in this case?
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build(); // is this the right thing to return?
            }

            do {
                nextUserResponse = new UserResponse();
                // nextUserResponse.setEmail(resultSet.getString("email")); Do not return email!
                nextUserResponse.setUserId(resultSet.getString("user_id"));
                nextUserResponse.setName(resultSet.getString("name"));
                nextUserResponse.setPreferredGender(resultSet.getString("preferred_gender"));
                nextUserResponse.setGender(resultSet.getString("gender"));
                nextUserResponse.setFavGenre(resultSet.getString("fav_genre"));
                // saves birthday as a string if not null
                nextUserResponse.setBirthday(Objects.toString(resultSet.getDate("birthday")));
                nextUserResponse.setBio(resultSet.getString("bio"));
                // nextUserResponse.setZipcode(resultSet.getString("zipcode")); Do not return zipcode!
                entries.add(nextUserResponse);
            } while(resultSet.next());
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(entries).build();
    }
}