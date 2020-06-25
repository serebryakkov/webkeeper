package bot.hostkeeper.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandDao {

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
