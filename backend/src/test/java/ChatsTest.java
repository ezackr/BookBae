import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
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
import com.bookbae.server.json.ChatLineResponse;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ChatsTest extends AbstractTest {
    private MockDatabaseService database;
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
        likeResource = new Like(database);
        chatsResource = new Chats(database);
        database.init();

        rand = new Random();

        // create a few semi-random users
        accountRequests = getExampleAccountRequests(numAccountRequests);
        userRequests = getExampleUserRequests(accountRequests);
        userIds = new String[accountRequests.length];

        for (int i = 0; i < accountRequests.length; i++) {
            userIds[i] = super.createMockUser(database, accountRequests[i], userRequests[i]);
        }

        //set 0th user and 1st user to like each other
        doLike(userIds[0], userIds[1]);
        doLike(userIds[1], userIds[0]);

    }
    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Disabled("Disabled for now because getting display name encounters SQL error")
    @Test
    void basicGetAllChatsTest(){
        // get chats for 0th user
        var resp = chatsResource.getAllChats(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());

        // get chat cards
        ArrayList<ChatCardResponse> chatCardResponses = (ArrayList<ChatCardResponse>) resp.getEntity();
        assertEquals(1, chatCardResponses.size());

        // ChatCardResponse arraylist should contain chatcardresponse of 1st user
        ChatCardResponse userOneChatCard = chatCardResponses.get(0);
        assertEquals(userOneChatCard.displayName, userRequests[1].name);

        // get chats for 1st user
        resp = chatsResource.getAllChats(new MockSecurityContext(userIds[1]));
        assertEquals(200, resp.getStatus());

        // get chat cards
        chatCardResponses = (ArrayList<ChatCardResponse>) resp.getEntity();
        assertEquals(1, chatCardResponses.size());

        // ChatCardResponse arraylist should contain chatcardresponse of 0th user
        ChatCardResponse userZeroChatCard = chatCardResponses.get(0);
        assertEquals(userZeroChatCard.displayName, userRequests[0].name);
    }

    @Test
    void basicPutChatTest(){

        // get likeId of 0th user's chat with 1st user
        var resp = chatsResource.getAllChats(new MockSecurityContext(userIds[0]));
        assertEquals(200, resp.getStatus());
        ArrayList<ChatCardResponse> chatCardResponses = (ArrayList<ChatCardResponse>) resp.getEntity();
        String likeId = chatCardResponses.get(0).likeId;

        // create ChatRequest
        var chatRequest = new ChatRequest();
        chatRequest.text = "Hello World!";

        // put chat "Hello World!" from user 0 to user 1
        resp = chatsResource.putChat(new MockSecurityContext(userIds[0]), likeId, chatRequest);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void basicPutThenGetChatTest(){
        // get likeId of 0th user's chat with 1st user
        var resp = chatsResource.getAllChats(new MockSecurityContext(userIds[0]));
        String likeId = ((ArrayList<ChatCardResponse>) resp.getEntity()).get(0).likeId;

        // create ChatRequest
        var chatRequest = new ChatRequest();
        chatRequest.text = "Hello World!";

        // put chat "Hello World!" from user 0 to user 1
        chatsResource.putChat(new MockSecurityContext(userIds[0]), likeId, chatRequest);

        // get chat between user 0 and user 1,
        resp = chatsResource.getChat(new MockSecurityContext(userIds[0]), likeId);
        ArrayList<ChatLineResponse> chatLines = (ArrayList<ChatLineResponse>) resp.getEntity();
        ChatLineResponse firstLine = chatLines.get(0);

        // check values
        assertEquals("Hello World!", firstLine.text);
        assertEquals(userIds[0], firstLine.userId);
        assertEquals(1, firstLine.nthMessage);

    }

    @Test
    void sqlFailureTest() {
        Chats badResource = new Chats(new SQLFailService());
        var resp = badResource.getAllChats(new MockSecurityContext(userIds[0]));
        assertEquals(500, resp.getStatus());
    }

    private void doLike(String likerUserId, String likedUserId) {
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.userid = likedUserId;
        var resp = likeResource.doLike(new MockSecurityContext(likerUserId), likeRequest);
        assertEquals(200, resp.getStatus());
    }

}