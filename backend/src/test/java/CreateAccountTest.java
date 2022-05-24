import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.util.UUID;
import com.bookbae.server.RestApplication;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.json.AccountRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateAccountTest extends AbstractTest {

    private AccountRequest accountRequest;
    private MockDatabaseService database;
    private CreateAccount resource;

    @BeforeEach
    void init() {
        accountRequest = getExampleAccountRequest();
        database = new MockDatabaseService("createAccountTest");
        resource = new CreateAccount(database);
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void successfulCreationTest() {
        var resp = resource.tryCreate(accountRequest);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void duplicateAccountTest() {
        var firstTryResp = resource.tryCreate(accountRequest);
        assertEquals(200, firstTryResp.getStatus());
        var secondTryResp = resource.tryCreate(accountRequest);
        assertEquals(403, secondTryResp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        CreateAccount badResource = new CreateAccount(new SQLFailService());
        var resp = badResource.tryCreate(accountRequest);
        assertEquals(500, resp.getStatus());
    }

}