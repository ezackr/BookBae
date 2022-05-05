import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class MockDatabaseService implements DatabasePoolService {
    private BasicDataSource dataSource;

    private static String createTables = makeSQLH2Compatible(readFromFile("/database/CREATETABLES.sql"));
    private static String dropTables = readFromFile("/database/DROPTABLES.sql");
    public MockDatabaseService(String name) {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:" + name);
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);

    }

    private static String readFromFile (String fileName){
        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(fileName));
        } catch(IOException e){
            e.printStackTrace();
        }
        return fileContents;
    }

    // UNIQUEIDENTIFIER is Microsoft's naming convention, must change to UUID for H2
    private static String makeSQLH2Compatible(String str){
        return str.replace("UNIQUEIDENTIFIER", "UUID");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    public void init() {
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(makeMeHappy)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void teardown() {
        try (Connection conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(makeMeSad)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}