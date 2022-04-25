import org.junit.jupiter.api.Test;

import com.bookbae.server.RestApplication;
import com.bookbae.server.UserResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserResourceTest {
    @Test
    void helloWorldTest() {
        RestApplication application = new MockRestApplication();
        UserResource resource = new UserResource(application);
        assertEquals("hi!", resource.helloWorld());
    }

    @Test
    void getUserTest() {
        RestApplication application = new MockRestApplication();
        UserResource resource = new UserResource(application);
        assertEquals("{}", resource.getUser());
    }
}