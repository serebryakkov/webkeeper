package dao;

import entity.Host;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostDao {
    public void add(User user, String url) {
        String sql = "INSERT INTO hosts (url, uid) VALUES (?, ?)";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, url);
            pstmt.setLong(2, user.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Host getById(int id) {
        Host host = null;
        String sql = "SELECT id, url, available FROM hosts WHERE id = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                host.setAvailable(rs.getBoolean(3));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return host;
    }

    public List<Host> getAllByUserId(User user) {
        List<Host> result = new ArrayList<>();
        Host host;
        String sql = "SELECT id, url FROM hosts WHERE uid = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                result.add(host);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void remove(User user, int id) {
        String sql = "DELETE FROM hosts WHERE id = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
