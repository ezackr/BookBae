import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.File;
import jakarta.ws.rs.client.ClientBuilder;

import org.glassfish.embeddable.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    private static Deployer deployer;
    private static File archive;
    @BeforeAll
    static void initGlassfish() throws GlassFishException {
        var props = new GlassFishProperties();
        props.setPort("http-listener", 8008);
        var glassfish = GlassFishRuntime.bootstrap().newGlassFish(props);
        glassfish.start();
        deployer = glassfish.getDeployer();
        archive = new File(System.getProperty("bookbae.archive"));
        deployer.deploy(archive, "--contextroot=api", "--name=api");
    }

    @Test
    void testConnection() {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/recommends")
                        .request("application/json")
                        .get();
        assertEquals(401, res.getStatus());
    }

    @AfterAll
    static void teardownGlassfish() throws GlassFishException {
        deployer.undeploy("api");
    }
}