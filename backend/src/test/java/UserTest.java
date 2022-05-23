import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.RestApplication;
import com.bookbae.server.User;
import com.bookbae.server.Login;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserRequest;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;
import com.bookbae.server.json.AccountRequest;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.bookbae.server.service.SecretKeyServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private MockDatabaseService database;
    private User userResource;
    private AccountRequest accountRequest;
    private CreateAccount createAccountResource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("userTest");
        createAccountResource = new CreateAccount(database);
        userResource = new User(database);
        accountRequest = getExampleAccountRequest();
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
        String userId = createMockUser();
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
        String userId = createMockUser();
        assert !userId.equals("");

        // Put mock user
        var userReq = getExampleUserRequest(); // UserRequest object contains new user attributes
        var resp = userResource.putUser(new MockSecurityContext(userId), userReq);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void putThenGetUserTest() {
        // Create mock user
        String userId = createMockUser();
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
        createAccountResource.tryCreate(accountRequest);
        userResource = new User(new SQLFailService());
        var resp = userResource.getUser(new MockSecurityContext(UUID.randomUUID().toString()));
        assertEquals(500, resp.getStatus());
    }

    // create an account and get the user id of the created account
    private String createMockUser() {
        var keys = new SecretKeyServiceImpl();
        createAccountResource.tryCreate(accountRequest);
        var loginResource = new Login(database, keys);
        var acct = (LoginResponse) loginResource.tryLogin(accountRequest).getEntity();
        var token = acct.getAuthToken();
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(keys.getKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String subj = jws.getBody().getSubject();
        // assume ^ work
        return subj;
    }

    // creates AccountRequest with dummy data
    private AccountRequest getExampleAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }

    // creates UserRequest with dummy data
    private UserRequest getExampleUserRequest() {
        var req = new UserRequest();
        req.setEmail("email@uw.edu");
        req.setName("Name Name");
        req.setGender("F");
        req.setFavGenre("Horror");
        req.setBirthday("2000-01-01");
        req.setBio("bio");
        req.setZipcode("80210");
        return req;
    }

}