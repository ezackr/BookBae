import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.RestApplication;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.Login;
import com.bookbae.server.Preferences;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response;
import com.bookbae.server.json.AccountRequest;
import com.bookbae.server.json.LoginResponse;
import com.bookbae.server.json.PreferencesMessage;
import java.util.UUID;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import com.bookbae.server.service.SecretKeyServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreferencesTest {
    private MockDatabaseService database;
    private CreateAccount createAccountResource;

    private Login loginResource;
    private Preferences preferencesResource;

    private AccountRequest accountRequest;
    private String userId;
    private SecretKeyServiceImpl keys;

    @BeforeEach
    void init() {
        database = new MockDatabaseService("preferencesTest");
        createAccountResource = new CreateAccount(database);
        keys = new SecretKeyServiceImpl();
        loginResource = new Login(database, keys);
        preferencesResource = new Preferences(database);
        database.init();

        accountRequest = new AccountRequest();
        accountRequest.setEmail("test@example.com");
        accountRequest.setPassword("hunter2");
        userId = createMockUser();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void getPreferencesTest() {
        var resp = preferencesResource.getPreferences(new MockSecurityContext(userId));
        assertEquals(200, resp.getStatus());
        PreferencesMessage returnedPrefs = (PreferencesMessage) resp.getEntity();
        assertEquals(0, returnedPrefs.lowerAgeLimit);
        assertEquals(0, returnedPrefs.upperAgeLimit);
        assertEquals(0, returnedPrefs.withinXMiles);
        assertEquals("", returnedPrefs.preferredGender);

    }

    @Test
    void setPreferencesTest() {
        PreferencesMessage prefs = new PreferencesMessage();
        prefs.lowerAgeLimit = 30;
        prefs.upperAgeLimit = 40;
        prefs.withinXMiles = 20;
        prefs.preferredGender = "M";
        var resp = preferencesResource.setPreferences(new MockSecurityContext(userId), prefs);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void setThenGetPreferencesTest() {
        PreferencesMessage prefs = new PreferencesMessage();
        prefs.lowerAgeLimit = 30;
        prefs.upperAgeLimit = 40;
        prefs.withinXMiles = 20;
        prefs.preferredGender = "M";
        var resp = preferencesResource.setPreferences(new MockSecurityContext(userId), prefs);
        assertEquals(200, resp.getStatus());
        resp = preferencesResource.getPreferences(new MockSecurityContext(userId));
        assertEquals(200, resp.getStatus());

        PreferencesMessage returnedPrefs = (PreferencesMessage) resp.getEntity();
        assertEquals(prefs.lowerAgeLimit, returnedPrefs.lowerAgeLimit);
        assertEquals(prefs.upperAgeLimit, returnedPrefs.upperAgeLimit);
        assertEquals(prefs.withinXMiles, returnedPrefs.withinXMiles);
        assertEquals(prefs.preferredGender, returnedPrefs.preferredGender);
    }

    // create an account and get the user id of the created account
    private String createMockUser() {
        createAccountResource.tryCreate(accountRequest);
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

}