package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import com.bookbae.server.json.AccountCreationRequest;
import com.bookbae.server.json.AccountCreationResponse;
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
    public Response tryCreate(AccountCreationRequest req) {

        // new account data
        String phone = "1234567890"; // req.getPhone();
        String email = "email@uw.edu"; // req.getEmail();
        String password = "password"; // req.getPassword();
        String salt = BCrypt.gensalt();
        String hashedPw = BCrypt.hashpw(password, salt);
        UUID userId = UUID.randomUUID();

        try {
            Connection conn = this.database.getConnection();

            // insert user into db with default NULL for unset values
            String insertUserInfoString = "INSERT INTO user_info" +
                    " VALUES(?, NULL, NULL, ?, NULL, NULL, ?, NULL, NULL, NULL);";
            // PreparedStatement insertUserStatement = conn.prepareStatement(insertUserInfoString);
            // insertUserStatement.setString(1, userId);
            // insertUserStatement.setString(2, phone);
            // insertUserStatement.setString(3, email);
            // insertUserStatement.executeUpdate();

            String insertLoginInfoString = "INSERT INTO login_info VALUES (?, ?, ?);";
            // PreparedStatement insertLoginInfoStatement = conn.prepareStatement(insertLoginInfoString);
            // insertLoginInfoStatement.setString(1, salt);
            // insertLoginInfoStatement.setString(2, hashedPw);
            // insertLoginInfoStatement.setString(3, userId);
            // insertLoginInfoStatement.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
            return Response.serverError().build();
        }
        // Get UUID from above and return it
        // Possibly return more stuff in accountcreationresponse if frontend team requests it
        return Response.ok(new AccountCreationResponse(userId)).build();
    }
}