import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.UserRequest;
import com.bookbae.server.Login;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.User;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.UUID;
import java.util.Random;
import com.bookbae.server.service.SecretKeyServiceImpl;


public abstract class AbstractTest {

    static AccountRequest getExampleAccountRequest() {
        AccountRequest req = new AccountRequest();
        req.setEmail("example@email.com");
        req.setPassword("password");
        return req;
    }

    static UserRequest getExampleUserRequest() {
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

    // creates a given number of accountRequests with random emails and passwords
    static AccountRequest[] getExampleAccountRequests(int numRequests) {
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
    static UserRequest[] getExampleUserRequests(AccountRequest[] acctRequests) {
        UserRequest[] userRequests = new UserRequest[acctRequests.length];
        Random rand = new Random();

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
    static String createMockUser(DatabasePoolService database, boolean shouldPutUser) {
        return shouldPutUser ?
                createMockUser(database, getExampleAccountRequest(), getExampleUserRequest()) :
                createMockUser(database, getExampleAccountRequest());
    }

    static String createMockUser(DatabasePoolService database, AccountRequest accountRequest, UserRequest userRequest) {
        String userId = createMockUser(database, accountRequest);
        User userResource = new User(database);
        userResource.putUser(new MockSecurityContext(userId), userRequest);
        return userId;
    }

    static String createMockUser(DatabasePoolService database, AccountRequest accountRequest) {
        var keys = new SecretKeyServiceImpl();
        var createAccountResource = new CreateAccount(database);
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
        String userId = jws.getBody().getSubject();
        return userId;
    }

}