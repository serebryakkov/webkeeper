package bot.hostkeeper.entity.interfaces;

import bot.hostkeeper.entity.Host;
import bot.hostkeeper.entity.User;

public interface Monitor {
    void startMonitor(Host host, User user);
    void stopMonitor(Host host);
}
