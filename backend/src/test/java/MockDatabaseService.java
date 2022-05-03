import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MockDatabaseService implements DatabasePoolService {
    private BasicDataSource dataSource;
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
}