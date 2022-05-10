package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.ChatCardResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import jakarta.inject.Inject;
import java.util.ArrayList;

@SecuredResource
@Path("/chats")
public class Chats {
    private DatabasePoolService database;
    // TODO: It would be better to not split up the database calls this way but this is simple and good enough for now
    // return all likeids and liked_user_ids where the client user is the liker and the like is mutual
    private String getAllChatsWhereClientUserIsLikerString = "SELECT like_id, liked_user_id" +
            " FROM likes" +
            " WHERE is_mutual" +
            " AND liker_user_id = ?;";

    //returns all likeids and liker_usr_ids where the client user id the liked user and the like is mutual
    private String getAllChatsWhereClientUserIsLikedString = "SELECT like_id, liker_user_id" +
            " FROM likes" +
            " WHERE is_mutual" +
            " AND liked_user_id = ?;";

    private String getNameFromUserIdString = "SELECT name FROM user_info WHERE user_id = ?;";

    @Inject
    public Chats(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response getAllChats(@Context SecurityContext ctx) {
        String clientUserId = ctx.getUserPrincipal().getName();
        ArrayList<ChatCardResponse> entities = new ArrayList<>();
        ChatCardResponse nextChatCardResponse;

        try (Connection conn = this.database.getConnection()) {
            // get all ChatCardResponses for when user is liker
            PreparedStatement getAllChatsWhereClientUserIsLikerStatement = conn.prepareStatement(
                    getAllChatsWhereClientUserIsLikerString);
            getAllChatsWhereClientUserIsLikerStatement.setString(1, clientUserId);
            ResultSet resultSet = getAllChatsWhereClientUserIsLikerStatement.executeQuery();
            while (resultSet.next()) {

                // create response
                nextChatCardResponse = new ChatCardResponse();

                // set like_id
                nextChatCardResponse.setLikeId(resultSet.getString("like_id"));

                // obtain display name
                String otherUserId = resultSet.getString("liked_user_id");
                PreparedStatement getNameFromUserIdStatement = conn.prepareStatement(getNameFromUserIdString);
                getNameFromUserIdStatement.setString(1, otherUserId);
                ResultSet nameResultSet = getNameFromUserIdStatement.executeQuery();
                assert(nameResultSet.next());

                // set display name
                nextChatCardResponse.setDisplayName(nameResultSet.getString("name"));
                nameResultSet.close();

                // add to entities
                entities.add(nextChatCardResponse);
            }

            resultSet.close();

            // get all ChatCardResponses for when user is liked
            PreparedStatement getAllChatsWhereClientUserIsLikedStatement = conn.prepareStatement(
                    getAllChatsWhereClientUserIsLikedString);
            getAllChatsWhereClientUserIsLikedStatement.setString(1, clientUserId);
            resultSet = getAllChatsWhereClientUserIsLikedStatement.executeQuery();
            while (resultSet.next()) {

                // create response
                nextChatCardResponse = new ChatCardResponse();

                // set like_id
                nextChatCardResponse.setLikeId(resultSet.getString("like_id"));

                // obtain display name
                String otherUserId = resultSet.getString("liker_user_id");
                PreparedStatement getNameFromUserIdStatement = conn.prepareStatement(getNameFromUserIdString);
                getNameFromUserIdStatement.setString(1, otherUserId);
                ResultSet nameResultSet = getNameFromUserIdStatement.executeQuery();
                assert(nameResultSet.next());

                // set display name
                nextChatCardResponse.setDisplayName(nameResultSet.getString("name"));
                nameResultSet.close();

                // add to entities
                entities.add(nextChatCardResponse);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(entities).build();
    }

    @Path("/{chatId}")
    @GET
    @Produces("application/json")
    public Response getChat(@Context SecurityContext ctx, @PathParam("likeId") String likeId) {
        // be sure to check that the user
        try (Connection conn = this.database.getConnection()) {
            //sql
            // return all chat IDs
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

}