import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import com.bookbae.server.DatabasePoolService;
import com.bookbae.server.Email;
import com.bookbae.server.json.EmailResponse;
import com.bookbae.server.json.AccountRequest;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EmailTest extends AbstractTest {
    private MockDatabaseService database;
    private Email emailResource;

    @BeforeEach
    void init(){
        database=new MockDatabaseService("emailTest");
        emailResource = new Email(database);
        database.init();
    }

    @AfterEach
    void teardown() {database.teardown();}


    @Test
    void emailDoesNotExistTest() {
        var resp = emailResource.checkEmail("example@email.com");
        assertEquals(200, resp.getStatus());
        EmailResponse emailResponse = (EmailResponse) resp.getEntity();
        assertEquals(false, emailResponse.getDoesEmailExist());
    }

    @Test
    void emailDoesExistTest() {
        createMockUser(database, false);
        var resp = emailResource.checkEmail("example@email.com");
        assertEquals(200, resp.getStatus());
        EmailResponse emailResponse = (EmailResponse) resp.getEntity();
        assertEquals(true, emailResponse.getDoesEmailExist());
    }
}