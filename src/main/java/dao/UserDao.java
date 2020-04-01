package dao;

import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public void add(User user) {
        String sql = "INSERT INTO users (uid, username) VALUES (?, ?)";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getById(long userId) {
        User user = null;
        String sql = "SELECT username, state FROM users WHERE uid = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(1));
//                String state = rs.getString(2);
//                if (state != null) {
//                    user.setState(User.State.valueOf(state));
//                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
