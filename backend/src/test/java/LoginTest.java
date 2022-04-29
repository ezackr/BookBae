import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.bookbae.server.RestApplication;
import com.bookbae.server.Login;
import com.bookbae.server.json.LoginRequest;
import com.bookbae.server.json.LoginResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoginTest {

    private Login resource;
    private RestApplication application;

    @BeforeEach
    void init() {
        application = new MockRestApplication();
        resource = new Login(application);
    }

    @Test
    void loginTest() {
        var resp = resource.tryLogin(getRequest());
        var entity = (LoginResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertDoesNotThrow(() -> {
            Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(application.getKey()).build().parseClaimsJws(entity.getAuthToken());
        });
    }

    @Test
    void invalidLoginTest() {
        var req = getRequest();
        req.setUsername("bad username");
        var resp = resource.tryLogin(req);
        assertEquals(400, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        resource = new Login(new SQLFailRestApplication());
        var resp = resource.tryLogin(getRequest());
        assertEquals(500, resp.getStatus());
    }

    private LoginRequest getRequest() {
        var req = new LoginRequest();
        req.setUsername(UUID.randomUUID().toString());
        req.setPassword("hunter2");
        return req;
    }
}