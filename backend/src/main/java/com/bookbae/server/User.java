package com.bookbae.server;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;
import com.bookbae.server.security.SecuredResource;

@Path("/user")
public class User {
    
    private RestApplication application;

    @Inject
    public User(RestApplication application) {
        this.application = application;
    }

    @GET @Produces("text/plain")
    public String helloWorld() {
        return "hi!";
    }

    @Produces("application/json")
    @GET @Path("/settings")
    public String getUser() {
        return "{}";
    }

    @SecuredResource
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

    @Produces("application/json")
    @GET @Path("/application")
    public String getApplication() {
        return "{ \"application\": \"" + this.application.getString() + "\", " + "\"id\": \"" + this.application.toString() + "\"}";
    }

    @Produces("application/json")
    @GET @Path("/database")
    public String getDatabase() {
        String str;
        try {
            Connection conn = this.application.getConnection();
            //TODO: SQL stuff
            conn.close();
            str = "{\"connection\": \"" + conn.toString() + "\"}";
        } catch (SQLException e) {
            str = "{\"connection\": \"none\", \"error\": \"" + e.toString() + "\"}";
        }
        return str;
    }
}