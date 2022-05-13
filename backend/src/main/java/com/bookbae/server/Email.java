package com.bookbae.server;

import com.bookbae.server.json.EmailResponse;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;

@Path("/email")
public class Email {
    private DatabasePoolService database;
    @Inject
    public Email(DatabasePoolService database) {
        this.database = database;
    }

    @GET
    @Produces("application/json")
    public Response checkEmail(@QueryParam("email") String email) {
        boolean emailIsInDatabase = false;
        if(email == null) {
            return Response.status(404).build();
        }
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EmailResponse resp = new EmailResponse();
        resp.setIsEmailValid(emailIsInDatabase);
        return Response.ok(resp).build();
    }
}