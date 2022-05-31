import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.Recommends;
import com.bookbae.server.Preferences;
import com.bookbae.server.Book;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.PreferencesMessage;
import com.bookbae.server.json.BookList;
import com.bookbae.server.json.BookListEntry;
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
            userRequests[i].email = accountRequests[0].email;
            userRequests[i].name = "User " + i;
            userRequests[i].gender = i % 2 == 0 ? "M" : "F";
            userRequests[i].favGenre = "Comedy";
            userRequests[i].birthday = "199" + i + "-01-01";
            userRequests[i].bio = "Bio " + i;
            userRequests[i].zipcode = "1000" + i;
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
            int yearBorn = Integer.parseInt(userResponse.birthday.substring(0, 4));
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
            assertEquals("F", userResponse.gender);
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
        AccountRequest nonBinAccount = createNBAccountRequest();
        UserRequest nonBinUser = createNBUserRequest();
        createMockUser(database, nonBinAccount, nonBinUser);

        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.preferredGender = "NB_";
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        assertEquals(1, entities.size()); // should just return NB user
        assertEquals(nonBinUser.gender, entities.get(0).gender);
    }

    @Test
    void returnsCorrectBookTest() {
        // create and add NB user
        AccountRequest nonBinAccount = createNBAccountRequest();
        UserRequest nonBinUser = createNBUserRequest();
        String nbId = createMockUser(database, nonBinAccount, nonBinUser);

        // add book to NB user bookshelf
        Book bookResouce = new Book(database);
        BookList bl = new BookList();
        BookListEntry ble = new BookListEntry();
        ble.bookId = "111111111111";
        bl.entries.add(ble);
        bookResouce.addBooks(new MockSecurityContext(nbId), bl);

        // set preferences of 0-th user to NB
        PreferencesMessage userZeroPrefs = getAllInclusivePreferencesMessage();
        userZeroPrefs.preferredGender = "NB_";
        preferencesResource.setPreferences(new MockSecurityContext(userIds[0]), userZeroPrefs);

        // get all recommended users for user 0, should just be NB user
        var resp = recommendsResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();
        UserResponse nbUserResponse = entities.get(0);

        // should return correct book
        assertEquals(1, nbUserResponse.bookList.entries.size());
        assertEquals("111111111111", nbUserResponse.bookList.entries.get(0).bookId);
    }

    @Test
    void returnsCorrectOrderByGenre() {
        // create two NB users who both like Romance and are interested in all users
        AccountRequest nonBinAccount1 = createNBAccountRequest();
        UserRequest nonBinUser1 = createNBUserRequest();
        nonBinUser1.name = "NB User 1";
        nonBinUser1.favGenre = "Romance";
        String nbUserId1 = createMockUser(database, nonBinAccount1, nonBinUser1);
        preferencesResource.setPreferences(new MockSecurityContext(nbUserId1), getAllInclusivePreferencesMessage());

        AccountRequest nonBinAccount2 = createNBAccountRequest();
        UserRequest nonBinUser2 = createNBUserRequest();
        nonBinAccount2.email = "example2@gmail.com";
        nonBinUser2.email = "example2@gmail.com";
        nonBinUser2.name = "NB User 2";
        nonBinUser2.favGenre = "Romance";
        String nbUserId2 = createMockUser(database, nonBinAccount2, nonBinUser2);
        preferencesResource.setPreferences(new MockSecurityContext(nbUserId2), getAllInclusivePreferencesMessage());

        // get all recommended users for NB user 1
        var resp = recommendsResource.getRecommends(new MockSecurityContext(nbUserId1));
        ArrayList<UserResponse> entities = (ArrayList<UserResponse>) resp.getEntity();

        // should return all but NB user1, which includes all users in numAccountRequests and other NB user
        assertEquals(numAccountRequests + 1, entities.size());
        // should return other NB user first, since they both liked Romance
        assertEquals("NB User 2", entities.get(0).name);
    }

    private AccountRequest createNBAccountRequest(){
        AccountRequest nonBinAccount = new AccountRequest();
        nonBinAccount.email = "example@gmail.com";
        nonBinAccount.password = "password";
        return nonBinAccount;
    }
    private UserRequest createNBUserRequest(){
        UserRequest nonBinUser = new UserRequest();
        nonBinUser.email = "example@gmail.com";
        nonBinUser.name = "User";
        nonBinUser.gender = "NB";
        nonBinUser.favGenre = "Comedy";
        nonBinUser.birthday = "1990-01-01";
        nonBinUser.bio = "Bio";
        nonBinUser.zipcode = "1000";
        return nonBinUser;
    }

    @Test
    void sqlFailureTest() {
        Recommends badResource = new Recommends(new SQLFailService());
        var resp = badResource.getRecommends(new MockSecurityContext(userIds[0]));
        assertEquals(500, resp.getStatus());
    }

}