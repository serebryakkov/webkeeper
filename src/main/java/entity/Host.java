package entity;

import dao.HostDao;
import org.apache.commons.validator.routines.UrlValidator;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Host {
    private int id;
    private String url;
    private boolean available;
    private long uid;
    private Date lastTimeCheck;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            this.url = "http://" + url;

        this.url = url;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Date getLastTimeCheck() {
        return lastTimeCheck;
    }

    public void setLastTimeCheck(Date lastTimeCheck) {
        this.lastTimeCheck = lastTimeCheck;
    }

    // TODO реализовать метод для проверки на наличие.
    public static boolean exists(Host host) {
        return true;
    }

    public static boolean validateUrl(Host host) {
        return new UrlValidator().isValid(host.url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Host host = (Host) o;
        return uid == host.uid &&
                url.equals(host.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, uid);
    }

    public static void add(Host host) {
        new HostDao().add(host);
    }

    public static Host getById(int id) {
        return new HostDao().getById(id);
    }

    public static List<Host> getAll() {
        return new HostDao().getAll();
    }

    public static List<Host> getAllByUserId(User user) {
        return new HostDao().getAllByUserId(user);
    }

    public static void updateAvailable(Host host) {
        new HostDao().updateAvailable(host);
    }

    public static void remove(Host host) {
        new HostDao().remove(host);
    }
}
