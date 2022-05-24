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

public class LikeTest extends AbstractTest {
    private MockDatabaseService database;
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
        likeResource = new Like(database);
        database.init();

        // create liker
        likerAccountRequest = getLikerAccountRequest();
        likerUserRequest = getLikerUserRequest();
        likerUserId = createMockUser(database, likerAccountRequest, likerUserRequest);

        //create liked
        likedAccountRequest = getLikedAccountRequest();
        likedUserRequest = getLikedUserRequest();
        likedUserId = createMockUser(database, likedAccountRequest, likedUserRequest);
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

    @Test
    void sqlFailureTest() {
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.userid = likedUserId;

        Like badResource = new Like(new SQLFailService());
        var resp = badResource.doLike(new MockSecurityContext(likedUserId), likeRequest);
        assertEquals(500, resp.getStatus());
    }

    private AccountRequest getLikerAccountRequest() {
        var req = new AccountRequest();
        req.email = "liker@example.com";
        req.password = "badpassword!";
        return req;
    }

    private UserRequest getLikerUserRequest() {
        var req = new UserRequest();
        req.email = "liker@example.com";
        req.name = "Liker Name";
        req.gender = "F";
        req.favGenre = "Mystery"; //TODO: what genres has the front end come up with? What are the legal genres?
        req.birthday = "1998-10-31";
        req.bio = "Liker bio";
        req.zipcode = "12345";
        return req;
    }

    private AccountRequest getLikedAccountRequest() {
        var req = new AccountRequest();
        req.email = "liked@example.com";
        req.password = "anotherbadpassword!";
        return req;
    }

    private UserRequest getLikedUserRequest() {
        var req = new UserRequest();
        req.email = "liked@example.com";
        req.name = "Liked Name";
        req.gender = "M";
        req.favGenre = "Western"; //TODO: what genres has the front end come up with? What are the legal genres?
        req.birthday = "1999-02-11";
        req.bio = "Liked bio";
        req.zipcode = "12323";
        return req;
    }

}