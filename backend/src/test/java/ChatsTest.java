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
import com.bookbae.server.Chats;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.json.LikeRequest;
import com.bookbae.server.json.ChatRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserResponse;
import com.bookbae.server.json.ChatCardResponse;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChatsTest {
    private MockDatabaseService database;
    private SecretKeyService keys;
    private CreateAccount createAccountResource;
    private Login loginResource;
    private User userResource;
    private Like likeResource;
    private Chats chatsResource;
    private int numAccountRequests = 3; // arbitrarily set to 3
    private AccountRequest[] accountRequests;
    private UserRequest[] userRequests;
    private String[] userIds;
    private Random rand;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("chatsTest");
        createAccountResource = new CreateAccount(database);
        keys = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        userResource = new User(database);
        likeResource = new Like(database);
        chatsResource = new Chats(database);
        database.init();

        rand = new Random();

        // create a few semi-random users
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

    @Test
    void basicGetChatTest(){
        //set 0th user and 1st user to like each other
        doLike(userIds[0], userIds[1]);
        doLike(userIds[1], userIds[0]);

        // get chats for 0th user
        var resp = chatsResource.getAllChats(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());

        // get chat cards
        ArrayList<ChatCardResponse> chatCardResponses = (ArrayList<ChatCardResponse>) resp.getEntity();
        assertEquals(1, chatCardResponses.size());

        // ChatCardResponse arraylist should contain chatcardresponse of 1st user
        ChatCardResponse userOneChatCard = chatCardResponses.get(0);
        assertEquals(userOneChatCard.getDisplayName(), userRequests[1].getName());

        // get chats for 1st user
        resp = chatsResource.getAllChats(new MockSecurityContext(userIds[1]));
        assertEquals(200, resp.getStatus());

        // get chat cards
        chatCardResponses = (ArrayList<ChatCardResponse>) resp.getEntity();
        assertEquals(1, chatCardResponses.size());

        // ChatCardResponse arraylist should contain chatcardresponse of 0th user
        ChatCardResponse userZeroChatCard = chatCardResponses.get(0);
        assertEquals(userZeroChatCard.getDisplayName(), userRequests[0].getName());
    }
    // TODO: this is repeated in different forms throughout the tests, when we have time we should factor it out
    // create an account with the given AccountRequest, put the User

    @Test
    void putThenGetSpecificChatTest(){

    }
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
            userRequests[i].setPreferredGender("M_F_NB");
            userRequests[i].setGender("F");
            userRequests[i].setFavGenre("Comedy");
            userRequests[i].setBirthday("2000-01-01");
            userRequests[i].setBio("User " + i + " bio");
            userRequests[i].setZipcode((rand.nextInt(89999) + 10000) + ""); //10000 - 99999 zip codes
        }
        return userRequests;
    }

    private void doLike(String likerUserId, String likedUserId) {
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.setUserId(likedUserId);
        var resp = likeResource.doLike(new MockSecurityContext(likerUserId), likeRequest);
        assertEquals(200, resp.getStatus());
    }
}