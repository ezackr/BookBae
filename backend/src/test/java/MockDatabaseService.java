import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.net.URISyntaxException;

public class MockDatabaseService implements DatabasePoolService {
    private BasicDataSource dataSource;
    private static String createTables;
    private static String dropTables;

    static {
        createTables = makeSQLH2Compatible(readFromFile("CREATETABLES.sql"));
        dropTables = readFromFile("DROPTABLES.sql");
    }

    public MockDatabaseService(String name) {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:" + name);
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
    }

    private static String readFromFile(String fileName) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        String ret;
        try {
            var url = loader.getResource(fileName);
            var uri = url.toURI();
            ret = Files.readString(Path.of(uri));
        } catch (Exception e) {
            e.printStackTrace();
            ret = "";
        }
        return ret;
    }

    // UNIQUEIDENTIFIER is Microsoft's naming convention, must change to UUID for H2
    // for some reason Microsoft doesn't like auto_increment and H2 doesn't like Identity
    private static String makeSQLH2Compatible(String str){
        str = str.replace("UNIQUEIDENTIFIER default NEWID()", "UUID");
        str = str.replace("UNIQUEIDENTIFIER", "UUID");
        str = str.replace("INT IDENTITY(1,1) NOT NULL", "INT AUTO_INCREMENT");
        return str;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void init() {
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(createTables)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void teardown() {
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(dropTables)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}