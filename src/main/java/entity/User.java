package entity;

import java.util.List;

public class User {
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

    public enum State {
        NULL,
        SITE_ADDING
    }
}
