package dao;

import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.sql.*;

public class DAOFactory {

    public static Connection getConnection() {
        String db_url = System.getenv("DB_URL");
        String db_user = System.getenv("DB_USER");
        String db_password = System.getenv("DB_PASSWORD");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(db_url, db_user, db_password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
