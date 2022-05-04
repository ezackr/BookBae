import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class MockDatabaseService implements DatabasePoolService {
    private BasicDataSource dataSource;
    private static String makeMeHappy = "CREATE TABLE user_info\n" +
"(\n" +
"    user_id UUID NOT NULL,\n" +
"    name VARCHAR(64) NOT NULL,\n" +
"    gender VARCHAR(2) NOT NULL,\n" +
"    phone_num VARCHAR(15) NOT NULL,\n" +
"    fav_genre VARCHAR(20) NOT NULL,\n" +
"    birthday DATE NOT NULL,\n" +
"    email VARCHAR(254) NOT NULL,\n" +
"    zipcode CHAR(5) NOT NULL,\n" +
"    bio VARCHAR(500) NOT NULL,\n" +
"    PRIMARY KEY (user_id),\n" +
"    UNIQUE (phone_num),\n" +
"    UNIQUE (email)\n" +
");";
    private static String makeMeSad = "DROP TABLE user_info;";

    public MockDatabaseService() {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:tests");
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);
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