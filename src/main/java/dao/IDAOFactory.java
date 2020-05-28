package dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDAOFactory {
    Connection getConnection() throws SQLException;
}
