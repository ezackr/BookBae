import com.bookbae.server.DatabasePoolService;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLFailService implements DatabasePoolService {
    @Override
    public Connection getConnection() throws SQLException {
        throw new SQLException();
    }
}