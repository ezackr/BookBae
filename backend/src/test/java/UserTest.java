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
    private UUID userid;
    private CreateAccount createAccountResource;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("userTest");
        createAccountResource = new CreateAccount(database);
        userResource = new User(database);
        userid = UUID.randomUUID();
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    // This tests getting a basic user with only login information saved
    @Test
    void getUserTest() {
        var accountRequest = getAccountRequest();
        String userId = getMockUserId(accountRequest);
        assert !userId.equals("");
        var resp = userResource.getUser(new MockSecurityContext(userId));
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertEquals(accountRequest.getEmail() , entity.getEmail());
    }

    @Test
    void putUserTest() {
        createAccountResource.tryCreate(getAccountRequest());
        var req = getUserRequest();
        var resp = userResource.putUser(new MockSecurityContext(userid.toString()), req);
        var entity = (UserResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        createAccountResource.tryCreate(getAccountRequest());
        userResource = new User(new SQLFailService());
        var resp = userResource.getUser(new MockSecurityContext(userid.toString()));
        assertEquals(500, resp.getStatus());
    }

    private String getMockUserId(AccountRequest accountRequest) {
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

    private AccountRequest getAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }

    private UserRequest getUserRequest() {
        var req = new UserRequest();
        req.setEmail("email@uw.edu");
        req.setName("Name Name");
        req.setPreferredGender("F_NB");
        req.setGender("F");
        req.setFavGenre("Horror");
        req.setBirthday("2000-01-01");
        req.setBio("bio");
        req.setZipcode("80210");
        return req;
    }

}