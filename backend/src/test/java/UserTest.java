import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;
import jakarta.ws.rs.core.SecurityContext;
import com.bookbae.server.json.AccountRequest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private MockDatabaseService database;
    private User userResource;
    private UUID userid;
    private CreateAccount createAccountResource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("userTest");
        createAccountResource = new CreateAccount(database);
        userResource = new User(database);
        userid = UUID.randomUUID();
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void getUserTest() {
        createAccountResource.tryCreate(getRequest());
        var resp = userResource.getUser(new MockSecurityContext(userid.toString()));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
    }

    @Test
    void putUserTest() {
        createAccountResource.tryCreate(getRequest());
        var req = new UserRequest();
        req.setEmail("email@uw.edu");
        req.setName("Name Name");
        req.setPreferredGender("F_NB");
        req.setGender("F");
        req.setFavGenre("Horror");
        req.setBirthday("2000-01-01");
        req.setBio("bio");
        req.setZipcode("80210");

        var resp = userResource.putUser(new MockSecurityContext(userid.toString()), req);
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(req.getZipcode(), entity.getZipcode());
    }

    @Test
    void sqlFailureTest() {
        createAccountResource.tryCreate(getRequest());
        userResource = new User(new SQLFailService());
        var resp = userResource.getUser(new MockSecurityContext(userid.toString()));
        assertEquals(500, resp.getStatus());
    }

    private AccountRequest getRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }
}