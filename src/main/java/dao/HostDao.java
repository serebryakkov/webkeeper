package dao;

import server.Host;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HostDao {
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

    public Map<Integer, String> getAllByUserId(long userId) {
        Map<Integer, String> result = null;
        String sql = "SELECT id, url FROM hosts WHERE uid = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, userId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                result = new HashMap<>();
                result.put(rs.getInt(1), rs.getString(2));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
