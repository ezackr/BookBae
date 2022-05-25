package com.bookbae.server;

import com.bookbae.server.security.SecuredResource;
import com.bookbae.server.json.BookList;
import com.bookbae.server.json.BookListEntry;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.StringBuffer;
import jakarta.inject.Inject;

/**
 * Provides endpoints for retrieving, saving and removing books from a user's bookshelf.
 */
@SecuredResource
@Path("/book")
public class Book {
    private DatabasePoolService database;

    private static final String GET_BOOKS = "SELECT DISTINCT book_id " +
            "FROM user_book " +
            "WHERE user_id = ?;";

    private static final String ADD_BOOKS = "INSERT INTO user_book " +
            "VALUES ";

    private static final String REMOVE_BOOKS = "DELETE FROM user_book " +
            "WHERE user_id = ? " +
            "AND (";

    /**
     * Creates a Book resource that acts on the given database.
     * @param database
     */
    @Inject
    public Book(DatabasePoolService database) {
        this.database = database;
    }

    /**
     * Retrieves a list of book ids for a given user.
     * @param ctx A SecurityContext variable containing the user's id
     * @return If successful, returns a jakarta ResponseBuilder with an OK status that contains a list of book ids
     *         If unsuccessful, returns a jakarta ResponseBuilder with a server error status
     * @see <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a> for the specific values returned by the endpoint
     */
    @Path("/get")
    @GET
    @Produces("application/json")
    public Response getBooks(@Context SecurityContext ctx) {
        BookList newList = new BookList();
        String clientUserId = ctx.getUserPrincipal().getName();

        try (Connection conn = this.database.getConnection()) {
            PreparedStatement getBooksStatement = conn.prepareStatement(GET_BOOKS);
            getBooksStatement.setString(1, clientUserId);
            ResultSet resultSet = getBooksStatement.executeQuery();

            while(resultSet.next()){
                BookListEntry bookListEntry = new BookListEntry();
                bookListEntry.bookId = resultSet.getString("book_id");
                newList.entries.add(bookListEntry);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(newList).build();
    }

    /**
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @param toAddList A list of books to save to the user's bookshelf
     * @return If successful, returns a jakarta ResponseBuilder with an OK status that contains the most updated list of books in a user's bookshelf
     *         If unsuccessful, returns a jakarta ResponseBuilder with a server error status
     * @see <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a> for the specific values returned by the endpoint
     */
    @Path("/add")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response addBooks(@Context SecurityContext ctx, BookList toAddList) {

        // get current books and user id
        BookList currentList = (BookList) getBooks(ctx).getEntity();
        //TODO: can we pull the above out to a helper method
        String clientUserId = ctx.getUserPrincipal().getName();

        // nothing to add
        if (toAddList.entries.size() == 0)
            return Response.ok(currentList).build();

        // add list of books to insert statement
        StringBuffer addBooksBuffer = new StringBuffer(ADD_BOOKS);
        for (BookListEntry ble : toAddList.entries) {
            addBooksBuffer.append(String.format("('%s', '%s'),", ble.bookId, clientUserId));
        }

        //TODO: sanitize this better? IDK

        // remove last comma and add semicolon
        addBooksBuffer.deleteCharAt(addBooksBuffer.length() - 1);
        addBooksBuffer.append(";");

        try (Connection conn = this.database.getConnection()) {
            // update database
            PreparedStatement addBooksStatement = conn.prepareStatement(addBooksBuffer.toString());
            addBooksStatement.executeUpdate();

            // currentList now represents existing and added entries
            currentList.entries.addAll(toAddList.entries);

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(currentList).build();
    }

    /**
     *
     * @param ctx A SecurityContext variable containing the user's id
     * @param toRemoveList A list of books to remove from the user's bookshelf
     * @return If successful, returns a jakarta ResponseBuilder with an OK status that contains the most updated list of books in a user's bookshelf
     *         If unsuccessful, returns a jakarta ResponseBuilder with a server error status
     * @see <a href="https://github.com/ezackr/BookBae/blob/main/backend/README.md">Backend Readme</a> for the specific values returned by the endpoint
     */
    @Path("/remove")
    @PUT
    @Produces("application/json")
    @Consumes("application/json")
    public Response removeBooks(@Context SecurityContext ctx, BookList toRemoveList) {

        // get current books and user id
        BookList currentList = (BookList) getBooks(ctx).getEntity();
        String clientUserId = ctx.getUserPrincipal().getName();

        // nothing to remove
        if (toRemoveList.entries.size() == 0)
            return Response.ok(currentList).build();

        // add list of books to remove statement
        StringBuffer removeBooksBuffer = new StringBuffer(REMOVE_BOOKS);
        for (BookListEntry ble : toRemoveList.entries) {
            removeBooksBuffer.append(String.format("book_id = '%s' OR ", ble.bookId));
        }

        // remove last OR and add parenthesis, semicolon
        removeBooksBuffer.delete(removeBooksBuffer.length() - 3, removeBooksBuffer.length() - 1);
        removeBooksBuffer.append(");");

        try (Connection conn = this.database.getConnection()) {
            // update database
            PreparedStatement removeBooksStatement = conn.prepareStatement(removeBooksBuffer.toString());
            removeBooksStatement.setString(1, clientUserId);
            removeBooksStatement.execute();

            // currentList now represents existing books after remove operation
            // because BookListEntry does not override equality method, cannot use removeAll within Collections
            // to calculate currentList
            currentList = (BookList) getBooks(ctx).getEntity();

        } catch (SQLException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
        return Response.ok(currentList).build();
    }
}