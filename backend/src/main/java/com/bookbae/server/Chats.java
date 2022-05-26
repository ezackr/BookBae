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

    private static final String PHOTO_BASE_URL = "https://bookbaephotos.blob.core.windows.net/userphotos/";

    private static final String GET_ALL_CHATS_FOR_USER = "SELECT like_id, liked_user_id, liker_user_id " +
            "FROM likes " +
            "WHERE is_mutual " +
            "AND (liker_user_id = ? OR liked_user_id = ?);";

    private static final String GET_NAME_FROM_USERID = "SELECT name " +
            "FROM user_info " +
            "WHERE user_id = ?;";

    private static final String GET_CHATS_BETWEEN_USERS = "SELECT * " +
            "FROM chat_line " +
            "WHERE like_id = ?;";

    private static final String INSERT_CHAT_BETWEEN_USERS = "INSERT INTO chat_line(line_text, timestamp, like_id, sender_user_id) " +
            // text, timestamp, like_id, sender_user_id
            "VALUES(?, ?, ?, ?);";

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
            PreparedStatement getAllChatsForUserStatement = conn.prepareStatement(
                    GET_ALL_CHATS_FOR_USER);
            getAllChatsForUserStatement.setString(1, clientUserId);
            getAllChatsForUserStatement.setString(2, clientUserId);
            ResultSet resultSet = getAllChatsForUserStatement.executeQuery();

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

                // set photoUrl
                nextChatCardResponse.photoUrl = PHOTO_BASE_URL + otherUserId;

                // obtain display name
                PreparedStatement getNameFromUserIdStatement = conn.prepareStatement(GET_NAME_FROM_USERID);
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
            PreparedStatement getChatsBetweenUsersStatement = conn.prepareStatement(GET_CHATS_BETWEEN_USERS);
            getChatsBetweenUsersStatement.setString(1, likeId);
            ResultSet resultSet = getChatsBetweenUsersStatement.executeQuery();
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
            PreparedStatement insertChatBetweenUsersStatement = conn.prepareStatement(INSERT_CHAT_BETWEEN_USERS);
            insertChatBetweenUsersStatement.setString(1, chatRequest.text);
            insertChatBetweenUsersStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            insertChatBetweenUsersStatement.setString(3, likeId);
            insertChatBetweenUsersStatement.setString(4, clientUserId);
            insertChatBetweenUsersStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }
}