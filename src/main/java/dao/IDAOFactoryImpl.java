package dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {

    private static IDAOFactoryImpl daoFactory;
    private final BasicDataSource pool;

    private IDAOFactoryImpl() {
        pool = new BasicDataSource();
        pool.setUrl(System.getenv("DB_URL"));
        pool.setUsername(System.getenv("DB_USER"));
        pool.setPassword(System.getenv("DB_PASSWORD"));
        pool.setMinIdle(1);
        pool.setMaxIdle(18);
        pool.setInitialSize(1);
    }

    public static IDAOFactoryImpl getInstance() {
        if (daoFactory == null) {
            daoFactory = new IDAOFactoryImpl();
        }
        return daoFactory;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }
}
