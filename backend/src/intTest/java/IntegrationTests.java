package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.File;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.embeddable.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegrationTests {
    private static Deployer deployer;
    private static File archive;
    private static String userInfo = "{\"email\": \"test3@example.com\", \"password\": \"hunter2\"}";

    @BeforeAll
    static void initGlassfish() throws GlassFishException {
        var props = new GlassFishProperties();
        props.setPort("http-listener", 8008);
        var glassfish = GlassFishRuntime.bootstrap().newGlassFish(props);
        glassfish.start();
        deployer = glassfish.getDeployer();
        System.clearProperty("javax.net.ssl.trustStore");
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

    @Test
    void loginTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var om = new ObjectMapper();
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
    }

    @Test
    void getUserTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var om = new ObjectMapper();
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var user_res = client.target("http://localhost:8008/api/v1/user")
                             .request("application/json")
                             .header("Authorization", header)
                             .buildGet()
                             .invoke();
        assertEquals(200, res.getStatus());
        var user_node = om.readTree(user_res.readEntity(String.class));
        String email = user_node.get("email").asText();
        assertEquals("test3@example.com", email);

    }

    @AfterAll
    static void teardownGlassfish() throws GlassFishException {
        deployer.undeploy("api");
    }
}