package dao;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {

    private static IDAOFactoryImpl daoFactory;
    private final PGPoolingDataSource pool;

    private IDAOFactoryImpl() {
        pool = new PGPoolingDataSource();
        pool.setServerName("ec2-54-195-247-108.eu-west-1.compute.amazonaws.com");
        pool.setDatabaseName("d186u2hno8v74l");
        pool.setUser(System.getenv("DB_USER"));
        pool.setPassword(System.getenv("DB_PASSWORD"));
        pool.setMaxConnections(18);
        pool.setInitialConnections(5);
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
