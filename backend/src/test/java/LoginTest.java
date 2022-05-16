import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.SecretKeyService;
import com.bookbae.server.service.SecretKeyServiceImpl;
import com.bookbae.server.Login;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoginTest {
    private MockDatabaseService database;
    private SecretKeyService keys;
    private AccountRequest accountRequest;
    private CreateAccount createAccountResource;
    private Login loginResource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("loginTest");
        createAccountResource = new CreateAccount(database);
        keys     = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        accountRequest = getExampleAccountRequest();
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void successfulLoginTest() {
        createAccountResource.tryCreate(accountRequest);
        var resp = loginResource.tryLogin(accountRequest);
        var entity = (LoginResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertDoesNotThrow(() -> {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keys.getKey()).build().parseClaimsJws(entity.getAuthToken());
        });
    }

    @Test
    void wrongEmailLoginTest() {
        createAccountResource.tryCreate(accountRequest);
        accountRequest.setEmail("wrongemail@gmail.com");
        var resp = loginResource.tryLogin(accountRequest);
        assertEquals(403, resp.getStatus());
    }

    @Test
    void wrongPasswordLoginTest() {
        createAccountResource.tryCreate(accountRequest);
        accountRequest.setPassword("wrongpassword");
        var resp = loginResource.tryLogin(accountRequest);
        assertEquals(403, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        createAccountResource.tryCreate(accountRequest);
        loginResource = new Login(new SQLFailService(), keys);
        var resp = loginResource.tryLogin(accountRequest);
        assertEquals(500, resp.getStatus());
    }

    private AccountRequest getExampleAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }
}