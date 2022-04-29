import com.bookbae.server.RestApplication;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLFailRestApplication extends RestApplication {
    public SQLFailRestApplication() {}

    @Override
    public Connection getConnection() throws SQLException {
        throw new SQLException();
    }
}