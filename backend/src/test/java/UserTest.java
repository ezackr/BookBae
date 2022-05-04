import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.UserRequest;
import jakarta.ws.rs.core.SecurityContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User resource;
    private UUID userid;
    @BeforeEach
    void init() {
        resource = new User(new MockDatabaseService());
        userid = UUID.randomUUID();
    }

    @Disabled
    @Test
    void getUserTest() {
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
    }

    @Disabled
    @Test
    void putUserTest() {
        var req = new UserRequest();
        req.setZipcode("80210"); // Is this right? yup!
        var resp = resource.putUser(new MockSecurityContext(userid.toString()), req);
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(req.getZipcode(), entity.getZipcode());
    }

    @Test
    void sqlFailureTest() {
        resource = new User(new SQLFailService());
        var resp = resource.getUser(new MockSecurityContext(userid.toString()));
        assertEquals(500, resp.getStatus());
    }
}