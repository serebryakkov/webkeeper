package bot.hostkeeper.entity;

public interface Monitor {
    void startMonitor(Host host, User user);
    void stopMonitor(Host host);
}
