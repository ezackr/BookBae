package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import jakarta.inject.Inject;
import java.util.UUID;
import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;

@SecuredResource
@Path("/user")
public class User {
    
    private DatabasePoolService database;

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
        try {
            Connection conn = this.database.getConnection();
            // retrieve user info
            UUID userId = UUID.fromString(ctx.getUserPrincipal().getName()); //TODO: is there a way for getName to return as a UUID? no
            String retrieveUserInfoString = "SELECT * " +
                    "FROM user_info " +
                    "WHERE user_id = ?";
            // PreparedStatement retrieveUserInfoStatement = conn.prepareStatement(retrieveUserInfoString);
            // retrieveUserInfoStatement.setString(1, userId);
            // ResultSet resultSet = retrieveUserInfoStatement.executeQuery();

            // invalid user id
            // if(!resultSet.next()){
            //    resultSet.close();
            //    return Response.status(Response.Status.FORBIDDEN).build();
            //}

            // populate resp object
            // resp.setEmail(resultSet.getString("email"));
            // resp.setName(resultSet.getString("name"));
            // resp.setPreferredGender(resultSet.getString("preferred_gender"));
            // resp.setGender(resultSet.getString("gender"));
            //resp.setFavGenre(resultSet.getString("fav_genre"));
            // resp.setBirthday(resultSet.getDate("birthday"));
            // resp.setBio(resultSet.getString("bio"));
            // resp.setZipcode(resultSet.getString("zipcode"));

            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }

    @PUT
    @Produces("application/json")
    public Response putUser(@Context SecurityContext ctx, UserRequest req) {
        if(ctx == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        UserResponse resp = new UserResponse(req);
        try {
            Connection conn = this.database.getConnection();

            // update user info
            UUID userId = UUID.fromString(ctx.getUserPrincipal().getName());
            String updateUserInfoString = "UPDATE user_info " +
                    "SET name = ?, preferred_gender = ?, gender = ?, fav_genre = ?," +
                    "birthday = ?, bio = ?, phone_num = ?, email = ?, zipcode = ? " +
                    "WHERE used_id = ?";
            // PreparedStatement updateUserInfoStatement = conn.prepareStatement(updateUserInfoString);
            // updateUserInfoStatement.setString(1, req.getName());
            // updateUserInfoStatement.setString(2, req.getPreferredGender());
            // updateUserInfoStatement.setString(3, req.getGender());
            // updateUserInfoStatement.setString(4, req.getFavGenre());
            // updateUserInfoStatement.setDate(5, req.getBirthday());
            // updateUserInfoStatement.setString(6, req.getBio());
            // updateUserInfoStatement.setString(7, req.getPhoneNumber());
            // updateUserInfoStatement.setString(8, req.getEmail());
            // updateUserInfoStatement.setString(9, req.getZipcode());
            // updateUserInfoStatement.setString(10, userId);
            // updateUserInfoStatement.executeUpdate();

            // populate resp object TODO: rmeove this should be populated already
                    //See above where resp takes req as a constructor arg
            resp.setEmail(req.getEmail());
            resp.setName(req.getName());
            resp.setPreferredGender(req.getPreferredGender());
            resp.setGender(req.getGender());
            resp.setFavGenre(req.getFavGenre());
            resp.setBirthday(req.getBirthday());
            resp.setBio(req.getBio());
            resp.setZipcode(req.getZipcode());

            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        return Response.ok(resp).build();
    }
}