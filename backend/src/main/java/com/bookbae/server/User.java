package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Date;
import jakarta.inject.Inject;
import java.util.UUID;
import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;
import java.util.Objects;

/**
 * Provides endpoints for getting information about a user and alternating information about a user.
 *
 * <br>Click here for more details about what each endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@SecuredResource
@Path("/user")
public class User {
    
    private DatabasePoolService database;
    private static final String RETRIEVE_USER_INFO = "SELECT * " +
            "FROM user_info " +
            "WHERE user_id = ?;";
    private static final String UPDATE_USER_INFO = "UPDATE user_info " +
            "SET name = ?, gender = ?, fav_genre = ?," +
            "birthday = ?, bio = ?, email = ?, zipcode = ? " +
            "WHERE user_id = ?;";
    private static final String PHOTO_URL_BASE = "https://bookbaephotos.blob.core.windows.net/userphotos/";

    @Inject
    public User(DatabasePoolService database) {
        this.database = database;
    }

    /**
     * Retreives basic information about the client user
     * @param ctx A SecurityContext variable containing the user's id
     * @return If successful, returns a <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/UserResponse.java">UserResponse</a> object
     *      containing basic information about the client user, but not their user id
     *      <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @GET
    @Produces("application/json")
    public Response getUser(@Context SecurityContext ctx) {
        if(ctx == null) {
            // Shouldn't happen
            // TODO: log this condition
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserResponse resp = new UserResponse();
        try (Connection conn = this.database.getConnection()) {
            // retrieve user info
            String userId = ctx.getUserPrincipal().getName();
            resp.photoUrl = PHOTO_URL_BASE + userId.toUpperCase();
            PreparedStatement retrieveUserInfoStatement = conn.prepareStatement(RETRIEVE_USER_INFO);
            retrieveUserInfoStatement.setString(1, userId);
            ResultSet resultSet = retrieveUserInfoStatement.executeQuery();

            // invalid user id
             if(!resultSet.next()){
                 resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }

             // populate resp object
             resp.email = resultSet.getString("email");
             resp.name = resultSet.getString("name");
             resp.gender = resultSet.getString("gender");
             resp.favGenre = resultSet.getString("fav_genre");
             resp.birthday = Objects.toString(resultSet.getDate("birthday")); // saves birthday as a string if not null
             resp.bio = resultSet.getString("bio");
             resp.zipcode = resultSet.getString("zipcode");

             resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }

    /**
     * Sets a number of attributes for the client user.
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @param req An <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/UserRequest.java">UserRequest</a> object containing attributes to set for the client user
     * @return If successful, returns a <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/UserResponse.java">UserResponse</a> object
     *         containing the attributes set for the client user, including the generated user id
     *          <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response putUser(@Context SecurityContext ctx, UserRequest req) {
        if(ctx == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserResponse resp = new UserResponse(req);

        try (Connection conn = this.database.getConnection()) {
            // update user info
            String userId = ctx.getUserPrincipal().getName();
            resp.photoUrl = PHOTO_URL_BASE + userId.toUpperCase();
            PreparedStatement updateUserInfoStatement = conn.prepareStatement(UPDATE_USER_INFO);
            updateUserInfoStatement.setString(1, req.name);
            updateUserInfoStatement.setString(2, req.gender);
            updateUserInfoStatement.setString(3, req.favGenre);
            updateUserInfoStatement.setDate(4, java.sql.Date.valueOf(req.birthday));
            updateUserInfoStatement.setString(5, req.bio);
            updateUserInfoStatement.setString(6, req.email);
            updateUserInfoStatement.setString(7, req.zipcode);
            updateUserInfoStatement.setString(8, userId);
            updateUserInfoStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }
}