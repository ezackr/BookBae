import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class IntegrationTest {
    @Test
    void testIntegratedLogin() {
        assumeTrue(System.getProperty("bookbae.testServer") != null);
        assumeFalse(System.getProperty("bookbae.testServer").equals(""));
        assertEquals(System.getProperty("bookbae.testServer"), "http://mint:8080/api");
    }
}