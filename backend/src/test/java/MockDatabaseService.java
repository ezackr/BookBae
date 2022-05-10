import com.bookbae.server.DatabasePoolService;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;

public class MockDatabaseService implements DatabasePoolService {
    private BasicDataSource dataSource;

    //private static String createTables = makeSQLH2Compatible(readFromFile("../database/CREATETABLES.sql"));//"./../../../../database/CREATETABLES.sql"));
    //private static String dropTables = readFromFile("DROPTABLES.sql");//"./../../../../database/DROPTABLES.sql");
    private static String createTables = "CREATE TABLE user_info\n" +
            "(\n" +
            "  user_id UUID NOT NULL,\n" +
            "  name VARCHAR(64),\n" +
            "  gender VARCHAR(2),\n" +
            "  fav_genre VARCHAR(20),\n" +
            "  birthday DATE,\n" +
            "  email VARCHAR(254) NOT NULL,\n" +
            "  zipcode CHAR(5),\n" +
            "  bio VARCHAR(500),\n" +
            "  preferred_gender VARCHAR,\n" +
            "  phone_num VARCHAR(15),\n" +
            "  PRIMARY KEY (user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE prompt\n" +
            "(\n" +
            "  prompt_id TINYINT NOT NULL,\n" +
            "  question VARCHAR(100) NOT NULL,\n" +
            "  answer VARCHAR(250) NOT NULL,\n" +
            "  user_id UUID NOT NULL,\n" +
            "  PRIMARY KEY (prompt_id, user_id),\n" +
            "  FOREIGN KEY (user_id) REFERENCES user_info(user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE preference\n" +
            "(\n" +
            "  low_target_age TINYINT NOT NULL,\n" +
            "  high_target_age TINYINT NOT NULL,\n" +
            "  within_x_miles SMALLINT NOT NULL,\n" +
            "  user_id UUID NOT NULL,\n" +
            "  PRIMARY KEY (user_id),\n" +
            "  FOREIGN KEY (user_id) REFERENCES user_info(user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE user_book\n" +
            "(\n" +
            "  book_id VARCHAR(12) NOT NULL,\n" +
            "  user_id UUID NOT NULL,\n" +
            "  FOREIGN KEY (user_id) REFERENCES user_info(user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE login_info\n" +
            "(\n" +
            "  salt VARCHAR(29) NOT NULL,\n" +
            "  hash VARCHAR(60) NOT NULL,\n" +
            "  user_id UUID NOT NULL,\n" +
            "  PRIMARY KEY (user_id),\n" +
            "  FOREIGN KEY (user_id) REFERENCES user_info(user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE likes\n" +
            "(\n" +
            "  is_mutual BIT NOT NULL,\n" +
            "  like_id UUID NOT NULL,\n" +
            "  liker_user_id UUID NOT NULL,\n" +
            "  liked_user_id UUID NOT NULL,\n" +
            "  PRIMARY KEY (like_id),\n" +
            "  FOREIGN KEY (liker_user_id) REFERENCES user_info(user_id),\n" +
            "  FOREIGN KEY (liked_user_id) REFERENCES user_info(user_id)\n" +
            ");\n" +
            "\n" +
            "CREATE TABLE chat_line\n" +
            "(\n" +
            "  line_id INT NOT NULL,\n" +
            "  line_text TEXT NOT NULL,\n" +
            "  timestamp TIMESTAMP NOT NULL,\n" +
            "  like_id UUID NOT NULL,\n" +
            "  PRIMARY KEY (line_id),\n" +
            "  FOREIGN KEY (like_id) REFERENCES likes(like_id)\n" +
            ");";

    private static String dropTables = "DROP TABLE chat_line;\n" +
            "DROP TABLE likes;\n" +
            "DROP TABLE login_info;\n" +
            "DROP TABLE user_book;\n" +
            "DROP TABLE preference;\n" +
            "DROP TABLE prompt;\n" +
            "DROP TABLE user_info;\n";

    public MockDatabaseService(String name) {
        this.dataSource = new BasicDataSource();
        this.dataSource.setUrl("jdbc:h2:mem:" + name);
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setPoolPreparedStatements(true);

        // System.out.println(Path.of(ClassLoader.getSystemResource("CREATETABLES.sql").getFile())); //getClass().getResource("/CREATETABLES.sql").getFile());
    }

    private static String readFromFile (String fileName) {

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
        str = str.replace("UNIQUEIDENTIFIER default NEWID()", "UUID");
        str = str.replace("UNIQUEIDENTIFIER", "UUID");
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