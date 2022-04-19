package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;

@Path("/user")
public class UserResource {
    @GET @Produces("text/plain")
    public String helloWorld() {
        return "hi!";
    }

    @Produces("application/json")
    @GET @Path("/settings")
    public String getUser() {
        return "{}";
    }

    @Produces("application/json")
    @GET @Path("/bookshelf")
    public String getBookshelf() {
        return "{}";
    }

    @Produces("application/json")
    @GET @Path("/matches")
    public String getMatches() {
        return "{}";
    }
}