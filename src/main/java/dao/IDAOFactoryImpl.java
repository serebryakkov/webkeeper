package dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {
    private static final ComboPooledDataSource pool = new ComboPooledDataSource();

    static {
        pool.setMinPoolSize(1);
        pool.setMaxPoolSize(18);
        pool.setJdbcUrl(System.getenv("DB_URL"));
        pool.setUser(System.getenv("DB_USER"));
        pool.setPassword(System.getenv("DB_PASSWORD"));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
