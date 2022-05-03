import com.bookbae.server.RestApplication;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLFailService extends DatabasePoolService {
    public SQLFailService() {}

    @Override
    public Connection getConnection() throws SQLException {
        throw new SQLException();
    }
}