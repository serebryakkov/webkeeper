package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageDao {
    private IDAOFactory daoFactory = new IDAOFactoryImpl();

    public String getByCode(String code) {
        String helpText = null;
        String sql = "SELECT text FROM help_texts WHERE code = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, code);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                helpText = rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helpText;
    }
}
