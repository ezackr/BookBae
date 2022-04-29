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
            // insert new row (?) into login_data that has the hash and salt
            // Get UUID from newly created row
            // Possibly populate user_info as well
            conn.close();
        } catch (SQLException e) {
            return Response.serverError().build();
        }
        // Get UUID from above and return it
        // Possibly return more stuff in accountcreationresponse if frontend team requests it
        return Response.ok(new AccountCreationResponse(UUID.randomUUID())).build();
    }
}