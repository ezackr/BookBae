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
import com.bookbae.server.Recommends;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.json.LikeRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Random;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RecommendsTest {
    private MockDatabaseService database;
    private SecretKeyService keys;
    private CreateAccount createAccountResource;
    private Login loginResource;
    private User userResource;
    private Recommends recommendsResource;
    private int numAccountRequests = 20; // arbitrarily set to 20
    private AccountRequest[] accountRequests;
    private UserRequest[] userRequests;
    private String[] userIds;
    private Random rand;


    @BeforeEach
    void init() {
        database = new MockDatabaseService("recommendsTest");
        createAccountResource = new CreateAccount(database);
        keys = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        userResource = new User(database);
        recommendsResource = new Recommends(database);
        database.init();
        rand = new Random();

        // create a bunch of semi-random users
        accountRequests = getExampleAccountRequests(numAccountRequests);
        userRequests = getExampleUserRequests(accountRequests);
        userIds = new String[accountRequests.length];

        for (int i = 0; i < accountRequests.length; i++) {
            userIds[i] = createMockUser(accountRequests[i], userRequests[i]);
        }

    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    // later can be expanded to make sure it's returning the right users in the right order based on the algorithm used
    @Test
    void basicRecommendsTest() {
        // arbitrarily chose to get recommendations for the 0-th user
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();// should return all but the user themselves
        assertEquals(numAccountRequests - 1, entities.size());
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

    // creates a given number of accountRequests with random emails and passwords
    private AccountRequest[] getExampleAccountRequests(int numRequests) {
        AccountRequest[] acctRequests = new AccountRequest[numRequests];

        for (int i = 0; i < acctRequests.length; i++) {
            acctRequests[i] = new AccountRequest();
            // UUID used here to create random email and password, not for user id
            acctRequests[i].setEmail(UUID.randomUUID().toString() + "@example.com");
            acctRequests[i].setPassword(UUID.randomUUID().toString());
        }
        return acctRequests;
    }

    // creates semi-random UserRequests based off of the given AccountRequests
    private UserRequest[] getExampleUserRequests(AccountRequest[] acctRequests) {
        UserRequest[] userRequests = new UserRequest[acctRequests.length];

        for (int i = 0; i < userRequests.length; i++) {
            userRequests[i] = new UserRequest();
            userRequests[i].setEmail(acctRequests[i].getEmail());
            userRequests[i].setName("Name " + i);
            userRequests[i].setGender("F");
            userRequests[i].setFavGenre("Comedy");
            userRequests[i].setBirthday("2000-01-01");
            userRequests[i].setBio("User " + i + " bio");
            userRequests[i].setZipcode((rand.nextInt(89999) + 10000) + ""); //10000 - 99999 zip codes
        }
        return userRequests;
    }
}