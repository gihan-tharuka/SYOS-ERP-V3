import dao.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            SynexOutletStoreSystem synexOutletStoreSystem = new SynexOutletStoreSystem();
            synexOutletStoreSystem.startApp(connection);
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
