package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.LikeRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.UUID;
import jakarta.inject.Inject;

@SecuredResource
@Path("/like")
public class Like {
    private DatabasePoolService database;
    private static String checkForDuplicateLikeString = "SELECT * FROM likes" +
            // liker_user_id = client user, liked_user_id = other user
            // client user has liked other user before and was the first to like, may or may not be mutual
            " WHERE (liker_user_id = ? AND liked_user_id = ?)" +
            // liker_user_id = other user, liked_user_id = client user
            // client user has liked other user before and was second to like, is mutual
            " OR (liker_user_id = ? AND liked_user_id = ? AND is_mutual = 'y');";


    private static String checkIfOtherUserLikesClientUserString = "SELECT * FROM likes" +
            // liker_user_id = other user, liked_user_id = client user
            // other user has already liked client user
            " WHERE liker_user_id = ? AND liked_user_id = ?;";

    private static String updateMutualLikeString = "UPDATE likes" +
            " SET is_mutual = 'y'" +
            // liker_user_id = other user, liked_user_id = client user
            " WHERE liker_user_id = ? AND liked_user_id = ?;";

    private static String insertNonMutualLikeString = "INSERT INTO likes" +
            // is_mutual, like_id, liker_user_id, liked_user_id
            " VALUES('n', ?, ?, ?);";

    @Inject
    public Like(DatabasePoolService database) {
        this.database = database;
    }

    @PUT
    @Consumes("application/json")
    public Response doLike(@Context SecurityContext ctx, LikeRequest likeRequest) {
        try (Connection conn = this.database.getConnection()) {

            String clientUserId = ctx.getUserPrincipal().getName();
            String otherUserId = likeRequest.userid;

            // check if client user already likes other user
                // if so, then this is a duplicate call, so throw exception? do nothing?
                // if not, continue
            PreparedStatement checkForDuplicateLikeStatement =
                    conn.prepareStatement(checkForDuplicateLikeString);
            checkForDuplicateLikeStatement.setString(1, clientUserId);
            checkForDuplicateLikeStatement.setString(2, otherUserId);
            checkForDuplicateLikeStatement.setString(3, otherUserId);
            checkForDuplicateLikeStatement.setString(4, clientUserId);
            ResultSet resultSet = checkForDuplicateLikeStatement.executeQuery();
            conn.commit();

            // client has already liked other user before, what should we do in this case?
            if(resultSet.next()) {
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            resultSet.close();

            // check if other user likes client user
            PreparedStatement checkIfOtherUserLikesClientUserStatement =
                    conn.prepareStatement(checkIfOtherUserLikesClientUserString);
            checkIfOtherUserLikesClientUserStatement.setString(1, otherUserId);
            checkIfOtherUserLikesClientUserStatement.setString(2, clientUserId);
            resultSet = checkIfOtherUserLikesClientUserStatement.executeQuery();

            // the like is mutual
            if(resultSet.next()) {
                PreparedStatement updateMutualLikeStatement = conn.prepareStatement(updateMutualLikeString);
                updateMutualLikeStatement.setString(1, otherUserId);
                updateMutualLikeStatement.setString(2, clientUserId);
                updateMutualLikeStatement.executeUpdate();
                conn.commit();
                onMutualLike(otherUserId, clientUserId);

            }
            // the like is not mutual
            else {
                PreparedStatement insertNonMutualLikeStatement = conn.prepareStatement(insertNonMutualLikeString);
                insertNonMutualLikeStatement.setString(1, UUID.randomUUID().toString());
                insertNonMutualLikeStatement.setString(2, clientUserId);
                insertNonMutualLikeStatement.setString(3, otherUserId);
                insertNonMutualLikeStatement.execute();
                conn.commit();

            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    private void onMutualLike(String likerId, String likedId) {
        //make chat
        return;
    }
}