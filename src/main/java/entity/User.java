package entity;

public class User {
    private long uid;
    private String username;
    private State state;

    public long getId() {
        return uid;
    }

    public void setId(long id) {
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

    public enum State {
        NULL,
        SITE_ADDING
    }
}
