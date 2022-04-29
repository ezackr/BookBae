import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.json.UserResponse;
import jakarta.ws.rs.core.SecurityContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User resource;
    @BeforeEach
    void init() {
        resource = new User(new MockRestApplication());
    }

    @Test
    void getUserTest() {
        UUID userid = UUID.randomUUID();
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(userid.toString(), entity.getUserId());
        assertEquals(200, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        UUID userid = UUID.randomUUID();
        resource = new User(new SQLFailRestApplication());
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        assertEquals(500, resp.getStatus());
    }
}