import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.SecretKeyService;
import com.bookbae.server.service.SecretKeyServiceImpl;
import com.bookbae.server.Login;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoginTest {

    private DatabasePoolService database;
    private SecretKeyService keys;
    private Login resource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService();
        keys     = new SecretKeyServiceImpl();
        resource = new Login(database, keys);
    }

    @Disabled
    @Test
    void loginTest() {
        var resp = resource.tryLogin(getRequest());
        var entity = (LoginResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertDoesNotThrow(() -> {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keys.getKey()).build().parseClaimsJws(entity.getAuthToken());
        });
    }

    @Disabled
    @Test
    void sqlFailureTest() {
        resource = new Login(new SQLFailService(), keys);
        var resp = resource.tryLogin(getRequest());
        assertEquals(500, resp.getStatus());
    }

    private AccountRequest getRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }
}