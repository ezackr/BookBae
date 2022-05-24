package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.ChatCardResponse;
import com.bookbae.server.json.ChatLineResponse;
import com.bookbae.server.json.ChatRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import jakarta.inject.Inject;
import java.util.ArrayList;


@SecuredResource
@Path("/chats")
public class Chats {
    private DatabasePoolService database;
    // TODO: It would be better to not split up the database calls this way but this is simple and good enough for now
    // return all likeids and user_ids where the user is mutually liked
    private String getAllChatsWhereClientUserHasMutualLikeString = "SELECT like_id, liked_user_id, liker_user_id" +
            " FROM likes" +
            " WHERE is_mutual" +
            " AND (liker_user_id = ? OR liked_user_id = ?);";

    private String getNameFromUserIdString = "SELECT name" +
            " FROM user_info" +
            " WHERE user_id = ?;";

    private String getChatsBetweenTwoUsersString = "SELECT *" +
            " FROM chat_line" +
            " WHERE like_id = ?;";

    private String insertChatBetweenTwoUsersString = "INSERT INTO chat_line(line_text, timestamp, like_id, sender_user_id)" +
            // text, timestamp, like_id, sender_user_id
            " VALUES(?, ?, ?, ?);";

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
            // get all ChatCardResponses for when user has a mutual like
            PreparedStatement getAllChatsWhereClientUserHasMutualLikeStatement = conn.prepareStatement(
                    getAllChatsWhereClientUserHasMutualLikeString);
            getAllChatsWhereClientUserHasMutualLikeStatement.setString(1, clientUserId);
            getAllChatsWhereClientUserHasMutualLikeStatement.setString(2, clientUserId);
            ResultSet resultSet = getAllChatsWhereClientUserHasMutualLikeStatement.executeQuery();

            while (resultSet.next()) {

                // create response
                nextChatCardResponse = new ChatCardResponse();

                // set like_id
                nextChatCardResponse.likeId = resultSet.getString("like_id");

                // find out whether user is liked or liker
                String otherUserId;
                if(resultSet.getString("liker_user_id").equals(clientUserId)) { // client user is liker
                    otherUserId = resultSet.getString("liked_user_id"); // other user is liked
                } else { // client user is liked
                    otherUserId = resultSet.getString("liker_user_id"); // other user is liker
                }

                // obtain display name
                PreparedStatement getNameFromUserIdStatement = conn.prepareStatement(getNameFromUserIdString);
                getNameFromUserIdStatement.setString(1, otherUserId);
                ResultSet nameResultSet = getNameFromUserIdStatement.executeQuery();
                assert(nameResultSet.next());

                // set display name
                nextChatCardResponse.displayName = nameResultSet.getString("name");
                nameResultSet.close();

                // add to entities
                entities.add(nextChatCardResponse);
            }
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(entities).build();
    }

    @Path("/{chatId}")
    @GET
    @Produces("application/json")
    public Response getChat(@Context SecurityContext ctx, @PathParam("likeId") String likeId) {

        ArrayList<ChatLineResponse> entities = new ArrayList<>();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement getChatsBetweenTwoUsersStatement = conn.prepareStatement(getChatsBetweenTwoUsersString);
            getChatsBetweenTwoUsersStatement.setString(1, likeId);
            ResultSet resultSet = getChatsBetweenTwoUsersStatement.executeQuery();
            ChatLineResponse nextChatLine;

            while (resultSet.next()) {
                nextChatLine = new ChatLineResponse();
                nextChatLine.text = resultSet.getString("line_text");
                nextChatLine.nthMessage = resultSet.getInt("line_id");
                nextChatLine.timestamp = resultSet.getTimestamp("timestamp");
                nextChatLine.userId = resultSet.getString("sender_user_id");
                entities.add(nextChatLine);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(entities).build();
    }

    @Path("/{chatId}")
    @PUT
    @Produces("application/json")
    public Response putChat(@Context SecurityContext ctx, @PathParam("likeId") String likeId, ChatRequest chatRequest) {

        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            // text, timestamp, like_id, sender_user_id
            PreparedStatement insertChatBetweenTwoUsersStatement = conn.prepareStatement(insertChatBetweenTwoUsersString);
            insertChatBetweenTwoUsersStatement.setString(1, chatRequest.text);
            insertChatBetweenTwoUsersStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertChatBetweenTwoUsersStatement.setString(3, likeId);
            insertChatBetweenTwoUsersStatement.setString(4, clientUserId);
            insertChatBetweenTwoUsersStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}