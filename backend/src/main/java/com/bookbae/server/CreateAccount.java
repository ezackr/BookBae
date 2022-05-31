package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import com.bookbae.server.json.AccountRequest;
import java.util.UUID;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Provides an endpoint for creating an account.
 *
 * <br>Click here for more details about what the endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@Path("/create")
public class CreateAccount {

    private static final String CHECK_IF_USER_ALREADY_EXISTS = "SELECT * " +
            "FROM user_info " +
            "WHERE email = ?;";

    private static final String INSERT_USER_INFO = "INSERT INTO user_info" +
            " VALUES(?, NULL, NULL, NULL, NULL, ?, NULL, NULL, NULL);";
    private static final String INSERT_LOGIN_INFO = "INSERT INTO login_info " +
            "VALUES (?, ?, ?);";

    private static final String INSERT_NULL_PREFERENCES = "INSERT INTO preference " +
            "VALUES(0, 0, 0, 'M_F_NB', ?);";

    private DatabasePoolService database;

    @Inject
    public CreateAccount(DatabasePoolService database) {
        this.database = database;
    }

    /**
     * Creates a user account using the email and password contained in req
     * @param req An <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/AccountRequest.java">AccountRequest</a> object containing the email and password of the user to be created
     * @return If successful, returns a jakarta ResponseBuilder with an OK status.
     *         <br>If unsuccessful, returns a jakarta ResponseBuilder with a server error status.
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response tryCreate(AccountRequest req) {

        // new account data
        String email = req.email;
        String password = req.password;
        String salt = BCrypt.gensalt();
        String hashedPw = BCrypt.hashpw(password, salt);
        String userId = UUID.randomUUID().toString();

        try (Connection conn = this.database.getConnection()) {
            // Check if email exists in db first
            PreparedStatement checkIfUserAlreadyExistsStatement = conn.prepareStatement(CHECK_IF_USER_ALREADY_EXISTS);
            checkIfUserAlreadyExistsStatement.setString(1, email);
            ResultSet resultSet = checkIfUserAlreadyExistsStatement.executeQuery();

            // email already in use, cannot create user
            if (resultSet.next()) {
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            resultSet.close();

            // insert user into db with default NULL for unset values
            PreparedStatement insertUserStatement = conn.prepareStatement(INSERT_USER_INFO);
            insertUserStatement.setString(1, userId);
            insertUserStatement.setString(2, email);
            insertUserStatement.executeUpdate();

            PreparedStatement insertLoginInfoStatement = conn.prepareStatement(INSERT_LOGIN_INFO);
            insertLoginInfoStatement.setString(1, salt);
            insertLoginInfoStatement.setString(2, hashedPw);
            insertLoginInfoStatement.setString(3, userId);
            insertLoginInfoStatement.executeUpdate();

            // insert user into preferences
            PreparedStatement insertNullPreferencesStatement = conn.prepareStatement(INSERT_NULL_PREFERENCES);
            insertNullPreferencesStatement.setString(1, userId);
            insertNullPreferencesStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}