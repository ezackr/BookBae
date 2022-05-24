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

public class LoginTest extends AbstractTest {
    private MockDatabaseService database;
    private SecretKeyService keys;
    private AccountRequest accountRequest;
    private CreateAccount createAccountResource;
    private Login loginResource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("loginTest");
        keys     = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        accountRequest = getExampleAccountRequest();
        database.init();

        super.createMockUser(database, false);
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void successfulLoginTest() {
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
        accountRequest.email = "wrongemail@gmail.com";
        var resp = loginResource.tryLogin(accountRequest);
        assertEquals(403, resp.getStatus());
    }

    @Test
    void wrongPasswordLoginTest() {
        accountRequest.password = "wrongpassword";
        var resp = loginResource.tryLogin(accountRequest);
        assertEquals(403, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        Login badResource = new Login(new SQLFailService(), keys);
        var resp = badResource.tryLogin(accountRequest);
        assertEquals(500, resp.getStatus());
    }

}