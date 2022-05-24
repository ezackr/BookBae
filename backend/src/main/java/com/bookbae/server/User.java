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

@SecuredResource
@Path("/user")
public class User {
    
    private DatabasePoolService database;
    private static String retrieveUserInfoString = "SELECT * " +
            "FROM user_info " +
            "WHERE user_id = ?;";
    private static String updateUserInfoString = "UPDATE user_info " +
            "SET name = ?, gender = ?, fav_genre = ?," +
            "birthday = ?, bio = ?, email = ?, zipcode = ? " +
            "WHERE user_id = ?;";

    @Inject
    public User(DatabasePoolService database) {
        this.database = database;
    }

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
            PreparedStatement retrieveUserInfoStatement = conn.prepareStatement(retrieveUserInfoString);
            retrieveUserInfoStatement.setString(1, userId);
            ResultSet resultSet = retrieveUserInfoStatement.executeQuery();

            // invalid user id
             if(!resultSet.next()){
                 resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }

             // populate resp object
             resp.setEmail(resultSet.getString("email"));
             resp.setName(resultSet.getString("name"));
             resp.setGender(resultSet.getString("gender"));
             resp.setFavGenre(resultSet.getString("fav_genre"));
             resp.setBirthday(Objects.toString(resultSet.getDate("birthday"))); // saves birthday as a string if not null
             resp.setBio(resultSet.getString("bio"));
             resp.setZipcode(resultSet.getString("zipcode"));
             resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }

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
            PreparedStatement updateUserInfoStatement = conn.prepareStatement(updateUserInfoString);
            updateUserInfoStatement.setString(1, req.getName());
            updateUserInfoStatement.setString(2, req.getGender());
            updateUserInfoStatement.setString(3, req.getFavGenre());
            updateUserInfoStatement.setDate(4, java.sql.Date.valueOf(req.getBirthday()));
            updateUserInfoStatement.setString(5, req.getBio());
            updateUserInfoStatement.setString(6, req.getEmail());
            updateUserInfoStatement.setString(7, req.getZipcode());
            updateUserInfoStatement.setString(8, userId);
            updateUserInfoStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }
}