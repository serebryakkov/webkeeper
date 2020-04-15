package dao;

import entity.Host;
import entity.User;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HostDao {
    public void add(Host host) {
        String sql = "INSERT INTO hosts (url, uid) VALUES (?, ?)";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, host.getUrl());
            pstmt.setLong(2, host.getUid());
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

    public List<Host> getAll() {
        List<Host> allHosts = new ArrayList<>();
        Host host;
        String sql = "SELECT id, url, available FROM hosts";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                host.setAvailable(rs.getBoolean(3));
                allHosts.add(host);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allHosts;
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

    public void updateAvailable(Host host) {
        String sql = "UPDATE hosts SET available = ?, last_time_check = ? WHERE url = ? AND uid = ?";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, host.isAvailable());
            pstmt.setString(2, dateFormat.format(host.getLastTimeCheck()));
            pstmt.setString(3, host.getUrl());
            pstmt.setLong(4, host.getUid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
