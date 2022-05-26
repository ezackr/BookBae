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

/**
 * Provides an endpoint for one user to like another user.
 *
 * <br>Click here for more details about what the endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@SecuredResource
@Path("/like")
public class Like {
    private DatabasePoolService database;

    // check if this is a duplicate call
    private static String CHECK_FOR_DUPLICATE_LIKE = "SELECT *" +
            " FROM likes" +
            // liker_user_id = client user, liked_user_id = other user
            // client user has liked other user before and was the first to like, may or may not be mutual
            " WHERE (liker_user_id = ? AND liked_user_id = ?)" +
            // liker_user_id = other user, liked_user_id = client user
            // client user has liked other user before and was second to like, is mutual
            " OR (liker_user_id = ? AND liked_user_id = ? AND is_mutual = 'y');";

    private static String CHECK_IF_OTHER_USER_LIKES_CLIENT_USER = "SELECT * FROM likes" +
            // liker_user_id = other user, liked_user_id = client user
            // other user has already liked client user
            " WHERE liker_user_id = ? AND liked_user_id = ?;";

    private static String UPDATE_MUTUAL_LIKE = "UPDATE likes" +
            " SET is_mutual = 'y'" +
            // liker_user_id = other user, liked_user_id = client user
            " WHERE liker_user_id = ? AND liked_user_id = ?;";

    private static String INSERT_NON_MUTUAL_LIKE = "INSERT INTO likes" +
            // is_mutual, like_id, liker_user_id, liked_user_id
            " VALUES('n', ?, ?, ?);";

    @Inject
    public Like(DatabasePoolService database) {
        this.database = database;
    }

    /**
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @param likeRequest A <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/LikeRequest.java">LikeRequest</a> object containing the likeId of the user to be liked
     * @return If successful, returns a jakarta ResponseBuilder with an OK status.
     *         <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @PUT
    @Consumes("application/json")
    public Response doLike(@Context SecurityContext ctx, LikeRequest likeRequest) {
        try (Connection conn = this.database.getConnection()) {

            String clientUserId = ctx.getUserPrincipal().getName();
            String otherUserId = likeRequest.userid;

            // check if client user already likes other user
                // if so, then this is a duplicate call, throw exception
                // if not, continue
            PreparedStatement checkForDuplicateLikeStatement =
                    conn.prepareStatement(CHECK_FOR_DUPLICATE_LIKE);
            checkForDuplicateLikeStatement.setString(1, clientUserId);
            checkForDuplicateLikeStatement.setString(2, otherUserId);
            checkForDuplicateLikeStatement.setString(3, otherUserId);
            checkForDuplicateLikeStatement.setString(4, clientUserId);
            ResultSet resultSet = checkForDuplicateLikeStatement.executeQuery();

            // client has already liked other user before, currently throws exception
            if(resultSet.next()) {
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            resultSet.close();

            // check if other user likes client user
            PreparedStatement checkIfOtherUserLikesClientUserStatement =
                    conn.prepareStatement(CHECK_IF_OTHER_USER_LIKES_CLIENT_USER);
            checkIfOtherUserLikesClientUserStatement.setString(1, otherUserId);
            checkIfOtherUserLikesClientUserStatement.setString(2, clientUserId);
            resultSet = checkIfOtherUserLikesClientUserStatement.executeQuery();

            // the like is mutual
            if(resultSet.next()) {
                PreparedStatement updateMutualLikeStatement = conn.prepareStatement(UPDATE_MUTUAL_LIKE);
                updateMutualLikeStatement.setString(1, otherUserId);
                updateMutualLikeStatement.setString(2, clientUserId);
                updateMutualLikeStatement.executeUpdate();

                onMutualLike(otherUserId, clientUserId);

            }
            // the like is not mutual
            else {
                PreparedStatement insertNonMutualLikeStatement = conn.prepareStatement(INSERT_NON_MUTUAL_LIKE);
                insertNonMutualLikeStatement.setString(1, UUID.randomUUID().toString());
                insertNonMutualLikeStatement.setString(2, clientUserId);
                insertNonMutualLikeStatement.setString(3, otherUserId);
                insertNonMutualLikeStatement.execute();

            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok().build();
    }

    //TODO: @Joshua what are we doing with this?
    private void onMutualLike(String likerId, String likedId) {
        //make chat
        return;
    }
}