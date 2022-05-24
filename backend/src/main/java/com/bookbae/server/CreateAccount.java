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

@Path("/create")
public class CreateAccount {

    private static String checkIfUserAlreadyExistsString = "SELECT * FROM user_info WHERE email = ?;";

    private static String insertUserInfoString = "INSERT INTO user_info" +
            " VALUES(?, NULL, NULL, NULL, NULL, ?, NULL, NULL, NULL);";
    private static String insertLoginInfoString = "INSERT INTO login_info VALUES (?, ?, ?);";

    private static String insertNullPreferencesString = "INSERT INTO preference VALUES(0, 0, 0, 'M_F_NB', ?);";
    private DatabasePoolService database;

    @Inject
    public CreateAccount(DatabasePoolService database) {
        this.database = database;
    }

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
            // TODO: we can probably get rid of this now that there's an email endpoint
            // Check if email exists in db first
            PreparedStatement checkIfUserAlreadyExistsStatement = conn.prepareStatement(checkIfUserAlreadyExistsString);
            checkIfUserAlreadyExistsStatement.setString(1, email);
            ResultSet resultSet = checkIfUserAlreadyExistsStatement.executeQuery();

            // email already in use, cannot create user
            if (resultSet.next()) {
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            resultSet.close();

            // insert user into db with default NULL for unset values
            PreparedStatement insertUserStatement = conn.prepareStatement(insertUserInfoString);
            insertUserStatement.setString(1, userId);
            insertUserStatement.setString(2, email);
            insertUserStatement.executeUpdate();

            PreparedStatement insertLoginInfoStatement = conn.prepareStatement(insertLoginInfoString);
            insertLoginInfoStatement.setString(1, salt);
            insertLoginInfoStatement.setString(2, hashedPw);
            insertLoginInfoStatement.setString(3, userId);
            insertLoginInfoStatement.executeUpdate();

            // insert user into preferences
            PreparedStatement insertNullPreferencesStatement = conn.prepareStatement(insertNullPreferencesString);
            insertNullPreferencesStatement.setString(1, userId);
            insertNullPreferencesStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

        return Response.ok().build();
    }
}