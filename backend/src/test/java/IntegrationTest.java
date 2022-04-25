import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IntegrationTest {
    @Test
    void testIntegratedLogin() {
        assumeFalse(System.getProperty("bookbae.testServer").equals(""));
        assertEquals("http://localhost:8080/api", System.getProperty("bookbae.testServer"));
        //TODO: write real integration tests that make calls against the server
    }
}