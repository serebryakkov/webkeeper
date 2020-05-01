package entity;

import Util.MetaTagInspector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Monitor extends Thread {
    private static final List<Monitor> monitors = new ArrayList<>();

    static {
        List<Host> hosts = Host.getAll();
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                User user = new User();
                user.setUid(host.getUid());
                new Monitor(host, user);
            }
        }
    }

    private final Host host;
    private final User user;

    public Monitor(Host host, User user) {
        this.host = host;
        this.user = user;
        monitors.add(this);
        this.start();
    }

    private void checkHost(String hostUrl) {
        try {
            URL url = new URL(hostUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(40000);
            int responseCode = urlConnection.getResponseCode();
            MetaTagInspector metaTagInspector = new MetaTagInspector();

            if (responseCode == 200 && metaTagInspector.checkMetaTag(hostUrl)) {
                host.setLastTimeCheck(new Date());
                if (!host.isAvailable()) {
                    host.setAvailable(true);
                    new Message(Message.Code.HOST_INFO, user, host).sendMessage();
                }
                Host.updateAvailable(host);
            } else if (responseCode == 301 || responseCode == 302) {
                String newUrl = urlConnection.getHeaderField("Location");
                checkHost(newUrl);
            } else {
                host.setLastTimeCheck(new Date());
                if (host.isAvailable()) {
                    host.setAvailable(false);
                    new Message(Message.Code.HOST_INFO, user, host).sendMessage();
                }
                Host.updateAvailable(host);
            }
        } catch (IOException e) {
            host.setLastTimeCheck(new Date());
            if (host.isAvailable()) {
                host.setAvailable(false);
                new Message(Message.Code.HOST_INFO, user, host).sendMessage();
            }
            Host.updateAvailable(host);
        }
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            checkHost(host.getUrl());

            try {
                TimeUnit.MINUTES.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopAndRemoveMonitor(Host host) {
        for (Monitor monitor : monitors) {
            if (monitor.host.equals(host)) {
                monitor.interrupt();
                monitors.remove(monitor);
            }
        }
    }
}
