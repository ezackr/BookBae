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
import com.bookbae.server.json.LoginRequest;
import com.bookbae.server.json.LoginResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Path("/login")
public class Login {
    private SecretKey key;
    private DatabasePoolService database;

    @Inject
    public Login(DatabasePoolService database, SecretKeyService keys) {
        this.database = database;
        this.key = keys.getKey();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response tryLogin(LoginRequest data) {
        if(!data.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        String userId = ""; // data.getUsername();
        String password = "password"; // data.getPassword();

        try {
            Connection conn = this.database.getConnection();

            // retrieve user's salt
            String retrieveSaltString = "SELECT salt FROM login_info WHERE user_id = ?";
            // PreparedStatement retrieveSaltStatement = conn.prepareStatement(retrieveSaltString);
            // retrieveSaltStatement.setString(1, userId);
            // ResultSet resultSet = retrieveSaltStatement.executeQuery();

           // salt not found; user does not exist
           // if (!resultSet.next()) {
           //     resultSet.close();
           //     return Response.status(Response.Status.FORBIDDEN).build();
           // }

            // salt found; user exists
            // String salt = resultSet.getString("salt");
            // resultSet.close();

            // check if login information is correct
            // String hashedPw = BCrypt.hashpw(password, salt);
             String checkLoginInfoString = "SELECT user_id FROM login_info WHERE" +
                    " user_id = ? AND password = ?;";
            //PreparedStatement checkLoginInfoStatement = conn.prepareStatement(checkLoginInfoString);
            // checkLoginInfoStatement.setString(1, userId);
            // checkLoginInfoStatement.setString(2, password);
            // resultSet = checkLoginInfoStatement.executeQuery();

            // password is incorrect
            //if (!resultSet.next()){
            //    resultSet.close();
            //    return Response.status(Response.Status.FORBIDDEN).build();
            //}

            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }

        String jws = Jwts.builder().setSubject(data.getUsername())
                         .signWith(this.key).compact();
        return Response.ok(new LoginResponse(jws)).build();
    }
}