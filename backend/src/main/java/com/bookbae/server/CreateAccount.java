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
import java.sql.PreparedStatement;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Path("/create")
public class CreateAccount {

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
        String email = "email@uw.edu"; // req.getEmail();
        String password = "password"; // req.getPassword();
        String phone = "deleteme";
        String salt = BCrypt.gensalt();
        String hashedPw = BCrypt.hashpw(password, salt);
        UUID userId = UUID.randomUUID();

        try {
            // maybe make a transaction here? IDK how transactions work
            Connection conn = this.database.getConnection();
            // Check if email exists in db first
            // insert user into db with default NULL for unset values
            String insertUserInfoString = "INSERT INTO user_info" +
                    " VALUES(?, NULL, NULL, ?, NULL, NULL, ?, NULL, NULL, NULL);";
            // PreparedStatement insertUserStatement = conn.prepareStatement(insertUserInfoString);
            // insertUserStatement.setString(1, userId.toString()); // Not right, userId is type uniqueidentifier I think
            // insertUserStatement.setString(2, phone);
            // insertUserStatement.setString(3, email);
            // insertUserStatement.executeUpdate();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user_info WHERE email = ?;");
            stmt.setString(1, req.getEmail());
            stmt.executeQuery();
            stmt.close();

            // String insertLoginInfoString = "INSERT INTO login_info VALUES (?, ?, ?);";
            // PreparedStatement insertLoginInfoStatement = conn.prepareStatement(insertLoginInfoString);
            // insertLoginInfoStatement.setString(1, salt);
            // insertLoginInfoStatement.setString(2, hashedPw);
            // insertLoginInfoStatement.setString(3, userId);
            // insertLoginInfoStatement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        // Get UUID from above and return it
        // Possibly return stuff in accountcreationresponse if frontend team requests it
        return Response.ok().build();
    }
}