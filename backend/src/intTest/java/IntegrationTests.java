package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import java.io.File;
import java.util.TreeSet;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.glassfish.embeddable.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class IntegrationTests {
    private static Deployer deployer;
    private static File archive;
    private static String userInfo = "{\"email\": \"test3@example.com\", \"password\": \"hunter2\"}";
    private static String createInfo = "{\"email\": \"test4@example.com\", \"password\": \"hunter2\"}";
    private static String setUser = "{\"email\": \"test3@example.com\", \"birthday\": \"1975-1-1\", \"bio\": \"hello world\"}";
    private static String books = "{\"entries\": [{\"bookId\": \"alpha\"}, {\"bookId\": \"beta\"}]}";
    private static final ObjectMapper om = new ObjectMapper();

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
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var user_res = client.target("http://localhost:8008/api/v1/user")
                             .request("application/json")
                             .header("Authorization", header)
                             .buildGet()
                             .invoke();
        assertEquals(200, user_res.getStatus());
        var user_node = om.readTree(user_res.readEntity(String.class));
        String email = user_node.get("email").asText();
        assertEquals("test3@example.com", email);
    }

    @Test
    void setUserTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var user_res = client.target("http://localhost:8008/api/v1/user")
                             .request("application/json")
                             .header("Authorization", header)
                             .buildPut(Entity.json(setUser))
                             .invoke();
        assertEquals(200, user_res.getStatus());
        var user_node = om.readTree(user_res.readEntity(String.class));
        String bio = user_node.get("bio").asText();
        assertEquals("hello world", bio);
        var user_res2 = client.target("http://localhost:8008/api/v1/user")
                              .request("application/json")
                              .header("Authorization", header)
                              .buildGet()
                              .invoke();
        assertEquals(200, user_res2.getStatus());
        var user_node2 = om.readTree(user_res2.readEntity(String.class));
        String bio2 = user_node2.get("bio").asText();
        assertEquals("hello world", bio2);
    }

    @Test
    void checkEmailTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/email?email=test3@example.com")
                        .request("application/json")
                        .buildGet()
                        .invoke();
        assertEquals(200, res.getStatus());
        var node = om.readTree(res.readEntity(String.class));
        boolean email = node.get("doesEmailExist").asBoolean();
        assertEquals(true, email);
        res = client.target("http://localhost:8008/api/v1/email?email=nonexistent@example.com")
                    .request("application/json")
                    .buildGet()
                    .invoke();
        assertEquals(200, res.getStatus());
        node = om.readTree(res.readEntity(String.class));
        email = node.get("doesEmailExist").asBoolean();
        assertEquals(false, email);
    }

    @Test
    void booksTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var getBookRes = client.target("http://localhost:8008/api/v1/book/get")
                               .request("application/json")
                               .header("Authorization", header)
                               .buildGet()
                               .invoke();
        assertEquals(200, getBookRes.getStatus());
        node = om.readTree(getBookRes.readEntity(String.class));
        assertTrue(node.has("entries"));
        assertEquals("", node.get("entries").asText());
        var setBookRes = client.target("http://localhost:8008/api/v1/book/add")
                               .request("application/json")
                               .header("Authorization", header)
                               .buildPut(Entity.json(books))
                               .invoke();
        assertEquals(200, setBookRes.getStatus());
        node = om.readTree(setBookRes.readEntity(String.class));
        assertTrue(node.has("entries"));
        node = node.get("entries");
        assertTrue(node.isArray());
        var it = node.elements();
        var bookIds = new TreeSet<String>();
        while(it.hasNext()) {
            var itEl = it.next();
            bookIds.add(itEl.get("bookId").asText());
        }
        assertEquals(2, bookIds.size());
        assertTrue(bookIds.contains("alpha"));
        assertTrue(bookIds.contains("beta"));
        var remBookRes = client.target("http://localhost:8008/api/v1/book/remove")
                               .request("application/json")
                               .header("Authorization", header)
                               .buildPut(Entity.json(books))
                               .invoke();
        assertEquals(200, remBookRes.getStatus());
        node = om.readTree(remBookRes.readEntity(String.class));
        assertTrue(node.has("entries"));
        assertEquals("", node.get("entries").asText());
    }

    @Test
    void createTest() {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/create")
                        .request("application/json")
                        .buildPost(Entity.json(createInfo))
                        .invoke();
        assertEquals(403, res.getStatus());
    }

    @Test
    void likeTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var recommendsRes = client.target("http://localhost:8008/api/v1/recommends")
                                  .request("application/json")
                                  .header("Authorization", header)
                                  .buildGet()
                                  .invoke();
        node = om.readTree(recommendsRes.readEntity(String.class));
        assumeTrue(node.hasNonNull(0));
        node = node.get(0); //UserResposne
        //Can't finish cuz you can't like multiple times and the testing database is persistent :/
    }

    @Test
    void recommendsTest() throws JsonProcessingException {
        var client = ClientBuilder.newClient();
        var res = client.target("http://localhost:8008/api/v1/login")
                        .request("application/json")
                        .buildPost(Entity.json(userInfo))
                        .invoke();
        assertEquals(200, res.getStatus());
        var node = om.readTree(res.readEntity(String.class));
        String token = node.get("authToken").asText();
        String header = "Bearer " + token;
        var recommendsRes = client.target("http://localhost:8008/api/v1/recommends")
                                  .request("application/json")
                                  .header("Authorization", header)
                                  .buildGet()
                                  .invoke();
        node = om.readTree(recommendsRes.readEntity(String.class));
        assertTrue(node.isArray());
    }

    @AfterAll
    static void teardownGlassfish() throws GlassFishException {
        deployer.undeploy("api");
    }
}