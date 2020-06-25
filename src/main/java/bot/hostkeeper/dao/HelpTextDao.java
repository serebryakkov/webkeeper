package bot.hostkeeper.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelpTextDao {

    public String getByCode(String code) {
        String helpText = null;
        String sql = "SELECT text FROM help_texts WHERE code = ?";

        try (Connection connection = DAOFactory.getConnection();
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

    public String getCommandTextByCode(String code) {
        String sql = "SELECT title FROM commands WHERE code = ?";
        String commandText = null;

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                commandText = rs.getString("title");
            }
        } catch (SQLException e) {

        }

        return commandText;
    }
}
