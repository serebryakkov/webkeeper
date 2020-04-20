package entity;

import dao.UserDao;

import java.util.List;

public class User {
    private static UserDao userDao;

    private long uid;
    private String username;
    private State state;
    private List<Host> hosts;

    public long getId() {
        return uid;
    }

    public void setUid(long id) {
        this.uid = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    // Метод добавляет пользователя в БД.
    public static void add(User user) {
        userDao = new UserDao();
        if (userDao.getById(user.getId()) == null) {
            userDao.add(user);
        }
        userDao = null;
    }

    public static State getUserState(User user) {
        return new UserDao().getUserState(user);
    }

    public static void updateUserState(User user) {
        userDao = new UserDao();
        userDao.updateUserState(user);
        userDao = null;
    }

    public enum State {
        NULL,
        SITE_ADDING
    }
}
