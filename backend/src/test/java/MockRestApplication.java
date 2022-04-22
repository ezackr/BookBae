import com.bookbae.server.RestApplication;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MockRestApplication extends RestApplication {
    private BasicDataSource mockDataSource;
    public MockRestApplication() {
        this.mockDataSource = new BasicDataSource();
        this.mockDataSource.setUrl("jdbc:h2:mem:tests");
        this.mockDataSource.setDriverClassName("org.h2.Driver");
        this.mockDataSource.setPoolPreparedStatements(true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.mockDataSource.getConnection();
    }
}