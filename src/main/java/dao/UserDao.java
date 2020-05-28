package dao;

import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private final IDAOFactory daoFactory = IDAOFactoryImpl.getInstance();

    public void add(User user) {
        String sql = "INSERT INTO users (uid, username) VALUES (?, ?)";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getUid());
            pstmt.setString(2, user.getUsername());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getById(long userId) {
        User user = null;
        String sql = "SELECT username, state FROM users WHERE uid = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString(1));
                user.setState(User.State.valueOf(rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void updateUserState(User user) {
        String sql = "UPDATE users SET state = ? WHERE uid = ?";

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getState().getStateName());
            pstmt.setLong(2, user.getUid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User.State getUserState(User user) {
        String sql = "SELECT state FROM users WHERE uid = ?";
        User.State state = null;

        try (Connection connection = daoFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getUid());

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String userState = rs.getString(1);
                if (userState.startsWith("ADD_META_TAG")) {
                    state = User.State.ADD_META_TAG;
                    state.setStateName(userState);
                } else {
                    state = User.State.valueOf(userState);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return state;
    }
}
