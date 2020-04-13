package entity;

import dao.HostDao;

import java.util.List;

public class Host {
    private int id;
    private String url;
    private boolean available;
    private long uid;

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


    //DAO методы
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

    public static void remove(User user, int id) {
        new HostDao().remove(user, id);
    }
}
