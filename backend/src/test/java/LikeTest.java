import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.SecretKeyService;
import com.bookbae.server.service.SecretKeyServiceImpl;
import com.bookbae.server.Login;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.User;
import com.bookbae.server.Like;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.json.LikeRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LikeTest {
    private MockDatabaseService database;
    private SecretKeyService keys;
    private AccountRequest accountRequest;
    private CreateAccount createAccountResource;
    private Login loginResource;
    private User userResource;
    private Like likeResource;
    private AccountRequest likerAccountRequest;
    private UserRequest likerUserRequest;
    private String likerUserId;
    private AccountRequest likedAccountRequest;
    private UserRequest likedUserRequest;
    private String likedUserId;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("likeTest");
        createAccountResource = new CreateAccount(database);
        keys     = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        userResource = new User(database);
        likeResource = new Like(database);
        database.init();

        // create liker
        likerAccountRequest = getLikerAccountRequest();
        likerUserRequest = getLikerUserRequest();
        likerUserId = createMockUser(likerAccountRequest, likerUserRequest);

        //create liked
        likedAccountRequest = getLikedAccountRequest();
        likedUserRequest = getLikedUserRequest();
        likedUserId = createMockUser(likedAccountRequest, likedUserRequest);
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    //should probably go back and add a test that makes sure that a like was added to the database
    @Test
    void basicLikeTest() {
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.userid = likedUserId;
        var resp = likeResource.doLike(new MockSecurityContext(likerUserId), likeRequest);
        assertEquals(200, resp.getStatus());

    }

    @Test
    void mutualLikeTest() {
        LikeRequest likeRequest = new LikeRequest();

        // liker likes liked request
        likeRequest.userid = likedUserId;
        var resp = likeResource.doLike(new MockSecurityContext(likerUserId), likeRequest);
        assertEquals(200, resp.getStatus());

        // liked likes liker request
        likeRequest.userid = likerUserId;
        resp = likeResource.doLike(new MockSecurityContext(likedUserId), likeRequest);
        assertEquals(200, resp.getStatus());

    }

    // create an account with the given AccountRequest, put the User
    private String createMockUser(AccountRequest accountRequest, UserRequest userRequest) {
        var resp = createAccountResource.tryCreate(accountRequest);
        assertEquals(200, resp.getStatus());
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
        String userId = jws.getBody().getSubject();
        // assume ^ work

        resp = userResource.putUser(new MockSecurityContext(userId), userRequest);
        assertEquals(200, resp.getStatus());

        return userId;
    }

    private AccountRequest getLikerAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("liker@example.com");
        req.setPassword("badpassword!");
        return req;
    }

    private UserRequest getLikerUserRequest() {
        var req = new UserRequest();
        req.setEmail("liker@example.com");
        req.setName("Liker Name");
        req.setGender("F");
        req.setFavGenre("Mystery"); //TODO: what genres has the front end come up with? What are the legal genres?
        req.setBirthday("1998-10-31");
        req.setBio("Liker bio");
        req.setZipcode("12345");
        return req;
    }

    private AccountRequest getLikedAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("liked@example.com");
        req.setPassword("anotherbadpassword!");
        return req;
    }

    private UserRequest getLikedUserRequest() {
        var req = new UserRequest();
        req.setEmail("liked@example.com");
        req.setName("Liked Name");
        req.setGender("M");
        req.setFavGenre("Western"); //TODO: what genres has the front end come up with? What are the legal genres?
        req.setBirthday("1999-02-11");
        req.setBio("Liked bio");
        req.setZipcode("12323");
        return req;
    }

}