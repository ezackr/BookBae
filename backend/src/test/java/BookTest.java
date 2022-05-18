import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.util.UUID;
import jakarta.ws.rs.core.SecurityContext;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.SecretKeyService;
import com.bookbae.server.service.SecretKeyServiceImpl;
import com.bookbae.server.RestApplication;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.Login;
import com.bookbae.server.Book;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.BookList;
import com.bookbae.server.json.BookListEntry;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookTest {

    private MockDatabaseService database;
    private AccountRequest accountRequest = getExampleAccountRequest();
    private Book bookResource;

    private String userId;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("bookTest");
        bookResource = new Book(database);
        database.init();

        userId = createMockUser();

    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void getEmptyBookListTest() {
        var resp = bookResource.getBooks(new MockSecurityContext(userId));
        assertEquals(200, resp.getStatus());
        BookList bookList = (BookList) resp.getEntity();
        assertEquals(0, bookList.entries.size());
    }

    @Test
    void addThenGetBooksTest() {
        BookList booksToAdd = new BookList();
        BookListEntry book1 = new BookListEntry();
        BookListEntry book2 = new BookListEntry();
        book1.bookId = "978073521129";
        book2.bookId = "978145556391";
        booksToAdd.entries.add(book1);
        booksToAdd.entries.add(book2);

        var resp = bookResource.addBooks(new MockSecurityContext(userId), booksToAdd);
        assertEquals(200, resp.getStatus());
        BookList addedBooks = (BookList) resp.getEntity();
        assertEquals(2, addedBooks.entries.size());
        assertEquals(true, addedBooks.entries.contains(book1));
        assertEquals(true, addedBooks.entries.contains(book2));

    }

    @Test
    void addThenRemoveAllThenGetTest() {
        BookList books = new BookList();
        BookListEntry book1 = new BookListEntry();
        BookListEntry book2 = new BookListEntry();
        book1.bookId = "978073521129";
        book2.bookId = "978145556391";
        books.entries.add(book1);
        books.entries.add(book2);
        bookResource.addBooks(new MockSecurityContext(userId), books);
        var resp = bookResource.removeBooks(new MockSecurityContext(userId), books);
        assertEquals(200, resp.getStatus());
        BookList resultList = (BookList) resp.getEntity();
        assertEquals(0, resultList.entries.size());
    }

    @Test
    void addThenRemoveOneThenGetTest() {
        BookList books = new BookList();
        BookListEntry book1 = new BookListEntry();
        BookListEntry book2 = new BookListEntry();
        book1.bookId = "978073521129";
        book2.bookId = "978145556391";
        books.entries.add(book1);
        books.entries.add(book2);
        bookResource.addBooks(new MockSecurityContext(userId), books);

        books.entries.remove(book1);
        var resp = bookResource.removeBooks(new MockSecurityContext(userId), books);
        assertEquals(200, resp.getStatus());

        BookList resultList = (BookList) resp.getEntity();
        assertEquals(1, resultList.entries.size());
        assertEquals(true, books.entries.contains(book2));
    }



    // create an account and get the user id of the created account
    private String createMockUser() {
        var keys = new SecretKeyServiceImpl();
        var createAccountResource = new CreateAccount(database);
        createAccountResource.tryCreate(accountRequest);
        var loginResource = new Login(database, keys);
        var acct = (LoginResponse) loginResource.tryLogin(accountRequest).getEntity();
        var token = acct.getAuthToken();
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(keys.getKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String subj = jws.getBody().getSubject();
        // assume ^ work
        return subj;
    }

    private AccountRequest getExampleAccountRequest() {
        AccountRequest req = new AccountRequest();
        req.setEmail("example@email.com");
        req.setPassword("password");
        return req;
    }
}