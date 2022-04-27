import org.junit.jupiter.api.Test;

import com.bookbae.server.RestApplication;
import com.bookbae.server.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    @Test
    void helloWorldTest() {
        RestApplication application = new MockRestApplication();
        User resource = new User(application);
        assertEquals("hi!", resource.helloWorld());
    }

    @Test
    void getUserTest() {
        RestApplication application = new MockRestApplication();
        User resource = new User(application);
        assertEquals("{}", resource.getUser());
    }
}