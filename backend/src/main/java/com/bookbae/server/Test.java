package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import com.bookbae.server.security.SecuredResource;

@Path("/test")
public class Test {
    @GET
    @Produces("application/json")
    public String getUser() {
        return "{\"a\": 1}";
    }
}