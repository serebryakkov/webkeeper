package dao;

import org.apache.commons.dbcp2.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class IDAOFactoryImpl implements IDAOFactory {

    private static IDAOFactoryImpl daoFactory;
    private final DataSource ds;

    private IDAOFactoryImpl() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(System.getenv("DB_URL"));
        dataSource.setUsername(System.getenv("DB_USER"));
        dataSource.setPassword(System.getenv("DB_PASSWORD"));
        dataSource.setMaxIdle(18);
        dataSource.setInitialSize(1);
        dataSource.setMaxConnLifetimeMillis(TimeUnit.MINUTES.toMillis(1));

        ds = dataSource;
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
