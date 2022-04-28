import org.junit.jupiter.api.Test;

import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.json.UserResponse;
import jakarta.ws.rs.core.SecurityContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    void getUserTest() {
        RestApplication application = new MockRestApplication();
        User resource = new User(application);
        UUID userid = UUID.randomUUID();
        SecurityContext ctx = new MockSecurityContext(userid.toString());
        var resp = resource.getUser(ctx);
        UserResponse entity = (UserResponse) resp.getEntity();
        assertEquals(userid.toString(), entity.getUserId());
    }
}