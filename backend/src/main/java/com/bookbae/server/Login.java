package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.UUID;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Path("/login")
public class Login {
    private SecretKey key;
    private DatabasePoolService database;
    private static String getUserIdFromEmailString = "SELECT user_id FROM user_info WHERE email = ?;";
    private static String retrieveSaltString = "SELECT salt " +
            "FROM login_info " +
            "WHERE user_id = ?";
    private static String checkLoginInfoString = "SELECT user_id " +
            "FROM login_info " +
            "WHERE user_id = ? " +
            "AND hash = ?;";

    @Inject
    public Login(DatabasePoolService database, SecretKeyService keys) {
        this.database = database;
        this.key = keys.getKey();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response tryLogin(AccountRequest data) {

        String email = data.getEmail();
        String password = data.getPassword();
        String userId = "";

        try {
            Connection conn = this.database.getConnection();

            // get user id
            PreparedStatement getUserIdStatement = conn.prepareStatement(getUserIdFromEmailString);
            getUserIdStatement.setString(1, email);
            ResultSet resultSet = getUserIdStatement.executeQuery();

            // user id not found; user does not exist
             if (!resultSet.next()) {
                 return Response.status(Response.Status.FORBIDDEN).build();
             }
            userId = resultSet.getString("user_id"); // not null! woo

            // get user's salt
            PreparedStatement retrieveSaltStatement = conn.prepareStatement(retrieveSaltString);
            retrieveSaltStatement.setString(1, userId);
            resultSet = retrieveSaltStatement.executeQuery();

           // salt not found
            if (!resultSet.next()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            // salt found; user exists
            String salt = resultSet.getString("salt");
            resultSet.close();

            // check if login information is correct
            String hashedPw = BCrypt.hashpw(password, salt);

            PreparedStatement checkLoginInfoStatement = conn.prepareStatement(checkLoginInfoString);
            checkLoginInfoStatement.setString(1, userId);
            checkLoginInfoStatement.setString(2, hashedPw);
            resultSet = checkLoginInfoStatement.executeQuery();

            // password is incorrect
            if (!resultSet.next()){
                resultSet.close();
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

        String jws = Jwts.builder().setSubject(userId)
                         .signWith(this.key).compact(); //TODO: add expiry date
        return Response.ok(new LoginResponse(jws)).build();
    }
}