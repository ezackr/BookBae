import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.Recommends;
import com.bookbae.server.Preferences;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.PreferencesMessage;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Random;
import java.util.ArrayList;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class RecommendsTest extends AbstractTest {
    private MockDatabaseService database;
    private Recommends recommendsResource;
    private Preferences preferencesResource;
    private int numAccountRequests = 10; // current tests rely on this being 10
    private AccountRequest[] accountRequests;
    private UserRequest[] userRequests;
    private String[] userIds;
    private Random rand;


    @BeforeEach
    void init() {
        database = new MockDatabaseService("recommendsTest");
        recommendsResource = new Recommends(database);
        preferencesResource = new Preferences(database);
        database.init();

        // create accounts with random username and password
        accountRequests = super.getExampleAccountRequests(numAccountRequests);
        userRequests = new UserRequest[numAccountRequests];
        userIds = new String[accountRequests.length];

        // create accounts such that:
        // - users are named "User i" depending on order
        // - even accounts are male, odd are female
        // - all users have comedy as their favorite genre
        // - user's birth date set to 199i (last number is i)
        // - bio = "Bio i"
        // - zipcode is 1000i (last number is i)
        for (int i = 0; i < accountRequests.length; i++) {
            userRequests[i] = new UserRequest();
            userRequests[i].setEmail(accountRequests[0].getEmail());
            userRequests[i].setName("User " + i);
            userRequests[i].setGender(i % 2 == 0 ? "M" : "F");
            userRequests[i].setFavGenre("Comedy");
            userRequests[i].setBirthday("199" + i + "-01-01");
            userRequests[i].setBio("Bio " + i);
            userRequests[i].setZipcode("1000" + i);
            userIds[i] = createMockUser(database, accountRequests[i], userRequests[i]);
        }
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    // returns a PreferencesMessage object that covers all users created in init()
    private PreferencesMessage getAllInclusivePreferencesMessage() {
        PreferencesMessage prefs = new PreferencesMessage();
        prefs.lowerAgeLimit = 18;
        prefs.upperAgeLimit = 100;
        prefs.withinXMiles = 100;
        prefs.preferredGender = "M_F_NB";
        return prefs;
    }

    void returnsNoUsersTest() {
        // set User 0's prefs to something no user matches
        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.lowerAgeLimit = 100;
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(0, entities.size());
    }


    @Test
    void returnsWithinAgeLimitTest() {
        // set User 0's prefs to users born between 1996 and 1999
        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.lowerAgeLimit = LocalDate.now().getYear() - 1999;
        userZeroPrefs.upperAgeLimit = LocalDate.now().getYear() - 1996;
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(4, entities.size()); // four users were born between 1996 and 1999

        for (UserResponse userResponse : entities) {
            int yearBorn = Integer.parseInt(userResponse.getBirthday().substring(0, 4));
            assertTrue(yearBorn >= 1996 && yearBorn <= 1999);
        }
    }

    @Test
    void returnsCorrectSingleGenderTest() {

        // set User 0's prefs to female users
        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.preferredGender = "F_";
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(numAccountRequests/2, entities.size()); // exactly half of the accounts are female
        for (UserResponse userResponse : entities) {
            assertEquals("F", userResponse.getGender());
        }
    }

    @Test
    void returnsCorrectMultipleGendersTest() {
        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(numAccountRequests - 1, entities.size()); // should return all but user himself
    }

    @Test
    void returnsCorrectNBTest() {
        AccountRequest nonBinAccount = new AccountRequest();
        nonBinAccount.setEmail("example@gmail.com");
        nonBinAccount.setPassword("password");

        UserRequest nonBinUser = new UserRequest();
        nonBinUser.setEmail("example@gmail.com");
        nonBinUser.setName("User");
        nonBinUser.setGender("NB");
        nonBinUser.setFavGenre("Comedy");
        nonBinUser.setBirthday("1990-01-01");
        nonBinUser.setBio("Bio");
        nonBinUser.setZipcode("1000");
        createMockUser(database, nonBinAccount, nonBinUser);

        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.preferredGender = "NB_";
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(1, entities.size()); // should just return NB user
        assertEquals(nonBinUser.getGender(), entities.get(0).getGender());
    }
}