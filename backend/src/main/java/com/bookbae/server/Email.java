package com.bookbae.server;

import com.bookbae.server.json.EmailResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.inject.Inject;

/**
 * Provides an endpoint to check if an email exsits.
 *
 * <br>Click here for more details about what the endpoint takes as input and gives as output: <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a>
 */
@Path("/email")
public class Email {
    private DatabasePoolService database;

    private static final String CHECK_EMAIL_EXISTS = "SELECT *" +
            " FROM user_info" +
            " WHERE email = ?;";
    @Inject
    public Email(DatabasePoolService database) {
        this.database = database;
    }

    /**
     * Check if a given email is already in use.
     *
     * @param email The email to check
     * @return An <a href="https://github.com/ezackr/BookBae/blob/main/backend/src/main/java/com/bookbae/server/json/EmailResponse.java">Email Response</a> object containing a boolean specifying whether or not the email exists
     */
    @GET
    @Produces("application/json")
    public Response checkEmail(@QueryParam("email") String email) {
        boolean emailIsInDatabase = false;
        if(email == null) {
            return Response.status(404).build();
        }
        try (Connection conn = this.database.getConnection()) {
            PreparedStatement checkEmailExistsStatement = conn.prepareStatement(CHECK_EMAIL_EXISTS);
            checkEmailExistsStatement.setString(1, email);
            ResultSet resultSet = checkEmailExistsStatement.executeQuery();

            // email already in use
            if(resultSet.next()) {
                emailIsInDatabase = true;
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        EmailResponse resp = new EmailResponse();
        resp.doesEmailExist = emailIsInDatabase;
        return Response.ok(resp).build();
    }
}