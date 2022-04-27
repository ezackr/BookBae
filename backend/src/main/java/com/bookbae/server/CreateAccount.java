package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import com.bookbae.server.json.AccountCredentials;
import com.bookbae.server.json.AccountCreationRequest;
import com.bookbae.server.json.AccountCreationResponse;
import java.util.UUID;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/create")
public class CreateAccount {

    private RestApplication application;

    @Inject
    public CreateAccount(RestApplication application) {
        this.application = application;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response tryCreate(AccountCreationRequest req) {
        try {
            Connection conn = this.application.getConnection();
            // generate salt
            // hash password from req with salt 
            // Generate UUID somehow
            // insert new row (?) into login_data that has the UUID, hash, salt
            // Possibly populate user_info as well
            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        return Response.ok(new AccountCreationResponse(UUID.randomUUID())).build();
    }
}