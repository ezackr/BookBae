import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserRequest;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;
import com.bookbae.server.json.AccountRequest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest extends AbstractTest {
    private MockDatabaseService database;
    private User userResource;
    private AccountRequest accountRequest = super.getExampleAccountRequest();

    @BeforeEach
    void init() {
        database = new MockDatabaseService("userTest");
        userResource = new User(database);
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    // This tests getting a basic user with only login information saved
    @Test
    void getBasicUserTest() {
        // Create mock user
        String userId = super.createMockUser(database, false);
        assert !userId.equals("");

        // Get mock user and check if response email matches
        var resp = userResource.getUser(new MockSecurityContext(userId));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(accountRequest.getEmail() , entity.getEmail());
    }

    @Test
    void putUserTest() {
        // Create mock user
        String userId = super.createMockUser(database, false);
        assert !userId.equals("");

        // Put mock user
        var userReq = getExampleUserRequest(); // UserRequest object contains new user attributes
        var resp = userResource.putUser(new MockSecurityContext(userId), userReq);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void putThenGetUserTest() {
        // Create mock user
        String userId = super.createMockUser(database, false);
        assert !userId.equals("");

        // Put mock user then get same user
        var userReq = getExampleUserRequest();
        var putResp = userResource.putUser(new MockSecurityContext(userId), userReq);
        assertEquals(200, putResp.getStatus());
        var getResp = userResource.getUser(new MockSecurityContext(userId));
        assertEquals(200, getResp.getStatus());

        //ensure response values are correct
        var getRespEntity = (UserResponse) getResp.getEntity();
        assertEquals(userReq.getEmail(), getRespEntity.getEmail());
        assertEquals(userReq.getName(), getRespEntity.getName());
        assertEquals(userReq.getGender(), getRespEntity.getGender());
        assertEquals(userReq.getFavGenre(), getRespEntity.getFavGenre());
        assertEquals(userReq.getBirthday(), getRespEntity.getBirthday());
        assertEquals(userReq.getBio(), getRespEntity.getBio());
        assertEquals(userReq.getZipcode(), getRespEntity.getZipcode());
    }

    @Test
    void sqlFailureTest() {
        userResource = new User(new SQLFailService());
        var resp = userResource.getUser(new MockSecurityContext(UUID.randomUUID().toString()));
        assertEquals(500, resp.getStatus());
    }
}