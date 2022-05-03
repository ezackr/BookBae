import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;
import com.bookbae.server.RestApplication;
import com.bookbae.server.CreateAccount;
import com.bookbae.server.json.AccountCreationRequest;
import com.bookbae.server.json.AccountCreationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateAccountTest {
    private CreateAccount resource;
    @BeforeEach
    void init() {
        resource = new CreateAccount(new MockDatabaseService());
    }

    @Test
    void creationTest() {
        var req = new AccountCreationRequest();
        var resp = resource.tryCreate(req);
        var entity = (AccountCreationResponse) resp.getEntity();
        assertEquals(200, resp.getStatus());
        assertNotNull(entity.getUsername());
        assertDoesNotThrow(() -> {
            UUID.fromString(entity.getUsername());
        });
        //TODO: database check?
        //this.application.getConnection()
        //assume
    }

    @Test
    void sqlFailureTest() {
        resource = new CreateAccount(new SQLFailService());
        var req = new AccountCreationRequest();
        var resp = resource.tryCreate(req);
        assertEquals(500, resp.getStatus());
    }
    
}