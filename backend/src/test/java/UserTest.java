import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;
import jakarta.ws.rs.core.SecurityContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User resource;
    @BeforeEach
    void init() {
        resource = new User(new MockDatabaseService());
    }

    @Test
    void getUserTest() {
        UUID userid = UUID.randomUUID();
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(userid.toString(), entity.getUserId());
    }

    @Test
    void putUserTest() {
        UUID userid = UUID.randomUUID();
        var req = new UserRequest();
        req.setUserId(userid.toString());
        req.setZipcode("80210"); // Is this right? yup!
        var resp = resource.putUser(new MockSecurityContext(userid.toString()), req);
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(req.getUserId(), entity.getUserId());
        assertEquals(req.getZipcode(), entity.getZipcode());
    }

    @Test
    void sqlFailureTest() {
        UUID userid = UUID.randomUUID();
        resource = new User(new SQLFailService());
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        assertEquals(500, resp.getStatus());
    }
}