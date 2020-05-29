package dao;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class IDAOFactoryImpl implements IDAOFactory {

    private static IDAOFactoryImpl daoFactory;
    private final DataSource ds;

    private IDAOFactoryImpl() {
        DriverManagerConnectionFactory connectionFactory =
                new DriverManagerConnectionFactory(
                        System.getenv("DB_URL"),
                        System.getenv("DB_USER"),
                        System.getenv("DB_PASSWORD")
                );

        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(
                        connectionFactory,
                        null);

        poolableConnectionFactory.setMaxConnLifetimeMillis(TimeUnit.MINUTES.toMillis(1));

        ObjectPool<PoolableConnection> connectionPool =
                new GenericObjectPool<>(poolableConnectionFactory);

        ds = new PoolingDataSource<>(connectionPool);
    }

    public static IDAOFactoryImpl getInstance() {
        if (daoFactory == null) {
            daoFactory = new IDAOFactoryImpl();
        }
        return daoFactory;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
