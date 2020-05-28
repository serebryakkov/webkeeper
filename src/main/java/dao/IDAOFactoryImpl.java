package dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {

    private static IDAOFactoryImpl daoFactory;
    private final BasicDataSource pool;

    private IDAOFactoryImpl() {
        BasicDataSource bs = new BasicDataSource();
        bs.setUrl(System.getenv("DB_URL"));
        bs.setUsername(System.getenv("DB_USER"));
        bs.setPassword(System.getenv("DB_PASSWORD"));
        bs.setMinIdle(1);
        bs.setMaxIdle(18);
        pool = bs;
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
