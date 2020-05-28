package dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {
    private static final ComboPooledDataSource pool = new ComboPooledDataSource();

    static {
        try {
            pool.setMinPoolSize(1);
            pool.setMaxPoolSize(18);
            pool.setDriverClass("org.postgresql.Driver");
            pool.setUser(System.getenv("DB_USER"));
            pool.setPassword(System.getenv("DB_PASSWORD"));
            pool.setJdbcUrl(System.getenv("DB_URL"));
            pool.setMaxStatements(0);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
