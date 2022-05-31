package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.security.ProxySecurityContext;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.BookList;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Objects;
import jakarta.inject.Inject;

/**
 * Provides an endpoint for getting a list of recommended users based on their preferences.
 *
 * <br>Click here for more details about what the endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@SecuredResource
@Path("/recommends")
public class Recommends {
    private DatabasePoolService database;

    private static final String GET_USER_FAV_GENRE = "SELECT fav_genre " +
            "FROM user_info " +
            "WHERE user_id = ?;";
    private static final String GET_USER_PREFERENCES = "SELECT * " +
            "FROM preference " +
            "WHERE user_id = ?;";

    // not static final because we need to swap out GETDATE for CURRENT_TIMESTAMP for testing
    private String get_recommendations = "SELECT * " +
            "FROM user_info " +
            "WHERE user_id != ? " +
            "AND DATEDIFF(year, birthday, GETDATE()) >= ? " +
            "AND DATEDIFF(year, birthday, GETDATE()) <= ? " +
            "AND ";

    private static final String ORDER_BY = " ORDER BY CASE WHEN fav_genre = ? THEN 1 ELSE 2 END, fav_genre;";

    @Inject
    public Recommends(DatabasePoolService database) {
        this.database = database;

        // GETDATE is Microsoft's syntax, CURRENT_TIMESTAMP is H2's syntax
        if (database.isMockDatabase()) {
            get_recommendations = get_recommendations.replace("GETDATE", "CURRENT_TIMESTAMP");
        }
    }

    /**
     * Retreives a list of recommended users based on the client user's preferences
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @return If successful, returns a jakarta ResponseBuilder with an OK status containing an ArrayList of
     *         <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/UserResponse.java">UserResponse</a> objects.
     *         <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @GET
    @Produces("application/json")
    public Response getRecommends(@Context SecurityContext ctx) {
        String clientUserId = ctx.getUserPrincipal().getName();
        ArrayList<UserResponse> entries = new ArrayList<>();
        UserResponse nextUserResponse;

        try (Connection conn = this.database.getConnection()) {

            // get user's favorite genre
            PreparedStatement getUserFavGenreStatement = conn.prepareStatement(GET_USER_FAV_GENRE);
            getUserFavGenreStatement.setString(1, clientUserId);
            ResultSet resultSet = getUserFavGenreStatement.executeQuery();
            resultSet.next();
            String favGenre = resultSet.getString("fav_genre");

            // get user's preferences
            PreparedStatement getUserPreferencesStatement = conn.prepareStatement(GET_USER_PREFERENCES);
            getUserPreferencesStatement.setString(1, clientUserId);
            resultSet = getUserPreferencesStatement.executeQuery();

            // if no preferences have been set
            if(!resultSet.next()) {
                return Response.ok(entries).build();
            }

            int lowerAge = resultSet.getInt("low_target_age");
            int upperAge = resultSet.getInt("high_target_age");
            // int withinXMiles = resultSet.getInt("within_x_miles");
            String[] preferredGenders = resultSet.getString("preferred_gender").split("_");

            // extend getRecommendsString to fit number of preferred genders
            StringBuffer getRecommendsBuffer = new StringBuffer(get_recommendations);
            getRecommendsBuffer.append("(");
            for(int i = 0; i < preferredGenders.length; i++) {
                getRecommendsBuffer.append(" gender = ? OR");
            }
            // remove last "OR", add ")", add ordering
            getRecommendsBuffer.delete(getRecommendsBuffer.length() - 3, getRecommendsBuffer.length());
            getRecommendsBuffer.append(")");
            getRecommendsBuffer.append(ORDER_BY);

            // get recommendations for the user based on their preferences
            PreparedStatement getRecommendedsStatement = conn.prepareStatement(getRecommendsBuffer.toString());
            getRecommendedsStatement.setString(1, clientUserId);
            getRecommendedsStatement.setInt(2, lowerAge);
            getRecommendedsStatement.setInt(3, upperAge);
            for(int i = 0; i < preferredGenders.length; i++) {
                getRecommendedsStatement.setString(4 + i, preferredGenders[i]);
            }
            getRecommendedsStatement.setString(4 + preferredGenders.length, favGenre);
            resultSet = getRecommendedsStatement.executeQuery();

            // resource to get other user's bookshelves
            Book bookResource = new Book(this.database);

            // load up users into json response
            while(resultSet.next()) {
                nextUserResponse = new UserResponse();
                nextUserResponse.userId = resultSet.getString("user_id");
                nextUserResponse.name = resultSet.getString("name");
                nextUserResponse.gender = resultSet.getString("gender");
                nextUserResponse.favGenre = resultSet.getString("fav_genre");
                // saves birthday as a string if not null
                nextUserResponse.birthday = Objects.toString(resultSet.getDate("birthday"));
                nextUserResponse.bio = resultSet.getString("bio");

                // get other user's books
                ProxySecurityContext pCtx = new ProxySecurityContext(ctx, nextUserResponse.userId);
                BookList userBooks = (BookList) bookResource.getBooks(pCtx).getEntity();
                nextUserResponse.bookList = userBooks;

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