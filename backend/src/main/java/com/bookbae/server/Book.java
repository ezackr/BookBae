package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.BookList;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.SQLException;
import jakarta.inject.Inject;

@SecuredResource
@Path("/book")
public class Book {
    private DatabasePoolService database;

    @Inject
    public Book(DatabasePoolService database) {
        this.database = database;
    }

    @Path("/get")
    @GET
    @Produces("application/json")
    public Response getBooks(@Context SecurityContext ctx) {
        BookList newList = new BookList();
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok(newList).build();
    }

    @Path("/add")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response addBooks(@Context SecurityContext ctx, BookList list) {
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok(list).build();
    }

    @Path("/remove")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response removeBooks(@Context SecurityContext ctx, BookList list) {
        BookList newList = new BookList();
        try (Connection conn = this.database.getConnection()) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Response.ok(newList).build();
    }
}