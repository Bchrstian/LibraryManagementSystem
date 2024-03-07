package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LibraryDatabase {
    static final String jdbcURL = "jdbc:postgresql://localhost:5432/java_2024_thursday";
    static final String username = "postgres";
    static final String passwd = "A$aprocky08";

    private LibraryDatabase() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcURL, username, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }
}
