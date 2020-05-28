package dao;

import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class IDAOFactoryImpl implements IDAOFactory {
    private static DataSource rawDs;
    private static DataSource ds;

    {
        try {
            rawDs = DataSources.unpooledDataSource(
                    System.getenv("DB_URL"),
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD"));

            ds = DataSources.pooledDataSource(rawDs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
