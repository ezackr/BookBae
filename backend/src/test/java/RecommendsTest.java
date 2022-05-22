import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.Recommends;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
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

public class RecommendsTest extends TestClass {
    private MockDatabaseService database;
    private Recommends recommendsResource;
    private int numAccountRequests = 20; // arbitrarily set to 20
    private AccountRequest[] accountRequests;
    private UserRequest[] userRequests;
    private String[] userIds;
    private Random rand;


    @BeforeEach
    void init() {
        database = new MockDatabaseService("recommendsTest");
        recommendsResource = new Recommends(database);
        database.init();

        // create a bunch of semi-random users
        accountRequests = super.getExampleAccountRequests(numAccountRequests);
        userRequests = super.getExampleUserRequests(accountRequests);
        userIds = new String[accountRequests.length];

        for (int i = 0; i < accountRequests.length; i++) {
            userIds[i] = createMockUser(database, accountRequests[i], userRequests[i]);
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

}