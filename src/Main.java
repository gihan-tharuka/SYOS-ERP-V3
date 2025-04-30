import dao.DatabaseConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        SynexOutletStoreSystem synexOutletStoreSystem = new SynexOutletStoreSystem();
        synexOutletStoreSystem.startApp(connection);
    }
}
