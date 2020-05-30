package dao;

import org.apache.commons.dbcp2.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class DAOFactory {
    private static final DataSource dataSource;

    static {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(System.getenv("DB_URL"));
        ds.setUsername(System.getenv("DB_USER"));
        ds.setPassword(System.getenv("DB_PASSWORD"));
        ds.setMaxIdle(18);
        ds.setInitialSize(5);
        ds.setMinIdle(0);
        ds.setMaxConnLifetimeMillis(TimeUnit.MINUTES.toMillis(1));

        dataSource = ds;
    }

    private DAOFactory() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
