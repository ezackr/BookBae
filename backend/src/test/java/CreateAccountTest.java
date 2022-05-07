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

public class CreateAccountTest {
    private MockDatabaseService database;
    private CreateAccount resource;
    @BeforeEach
    void init() {
        database = new MockDatabaseService("createAccountTest");
        resource = new CreateAccount(database);
        database.init();
    }

    @AfterEach
    void teardown() {
        database.teardown();
    }

    @Test
    void creationTest() {
        var req = getAccountRequest();
        var resp = resource.tryCreate(req);
        assertEquals(200, resp.getStatus());
    }

    @Test
    void sqlFailureTest() {
        resource = new CreateAccount(new SQLFailService());
        var req = getAccountRequest();
        var resp = resource.tryCreate(req);
        assertEquals(500, resp.getStatus());
    }

    private AccountRequest getAccountRequest() {
        var req = new AccountRequest();
        req.setEmail("test@example.com");
        req.setPassword("hunter2");
        return req;
    }
    
}