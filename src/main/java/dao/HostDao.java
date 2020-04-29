package dao;

import entity.Host;
import entity.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
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
        String sql = "SELECT id, url, available, uid, last_time_check FROM hosts WHERE id = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                host.setAvailable(rs.getBoolean(3));
                host.setUid(rs.getLong(4));
                host.setLastTimeCheck(new Date(rs.getTimestamp(5).getTime()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return host;
    }

    public List<Host> getAll() {
        List<Host> allHosts = new ArrayList<>();
        Host host;
        String sql = "SELECT id, url, available, uid FROM hosts";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                host.setAvailable(rs.getBoolean(3));
                host.setUid(rs.getLong(4));
                allHosts.add(host);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allHosts;
    }

    public List<Host> getAllByUserId(User user) {
        Host host;
        List<Host> result = new ArrayList<>();
        String sql = "SELECT id, url, uid FROM hosts WHERE uid = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, user.getId());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                host = new Host();
                host.setId(rs.getInt(1));
                host.setUrl(rs.getString(2));
                host.setUid(rs.getLong(3));
                result.add(host);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void updateAvailable(Host host) {
        String sql = "UPDATE hosts SET available = ?, last_time_check = ? WHERE url = ? AND uid = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, host.isAvailable());
            pstmt.setTimestamp(2, new Timestamp(host.getLastTimeCheck().getTime()));
            pstmt.setString(3, host.getUrl());
            pstmt.setLong(4, host.getUid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(Host host) {
        String sql = "DELETE FROM hosts WHERE id = ?";

        try (Connection connection = DAOFactory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, host.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws IOException {
//        Document doc = Jsoup.connect("http://insafe.cloud").get();
//        Elements listHead = doc.select("meta[name~=hostkeeper\\d+]");
//
//        for (Element element : listHead)
//            System.out.println(element);
//    }
}
