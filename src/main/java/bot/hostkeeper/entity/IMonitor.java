package bot.hostkeeper.entity;

public interface IMonitor {
    void startMonitor(Host host, User user);
    void stopMonitor(Host host);
}
