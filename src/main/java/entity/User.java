package entity;

import dao.UserDao;

import java.util.List;

public class User {
    private long uid;
    private String username;
    private State state;
    private List<Host> hosts;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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
        UserDao userDao = new UserDao();
        if (userDao.getById(user.getUid()) == null) {
            userDao.add(user);
        }
    }

    public static State getUserState(User user) {
        return new UserDao().getUserState(user);
    }

    // Метод обновляет состояние (state) пользователя в БД.
    public static void updateUserState(User user) {
        new UserDao().updateUserState(user);
    }

    public enum State {
        NULL("NULL"),
        SITE_ADDING("SITE_ADDING"),
        ADD_META_TAG("ADD_META_TAG_");

        State(String stateName) {
            this.stateName = stateName;
        }

        private String stateName;

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getStateName() {
            return stateName;
        }
    }
}
