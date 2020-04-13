package entity;

import server.Bot;
import ui.Message;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Monitor extends Thread {
    private static final List<Monitor> monitors = new ArrayList<>();
    private static Bot bot;

    static {
        List<Host> hosts = Host.getAll();
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                User user = new User();
                user.setUid(host.getUid());
                monitors.add(new Monitor(host, user));
            }
        }
    }

    private Host host;
    private User user;

    public Monitor(Host host, User user) {
        this.host = host;
        this.user = user;
        monitors.add(this);
        this.start();
    }

    public static void setBot(Bot bot) {
        bot = bot;
    }

    public static void stopAndRemoveMonitor() {
       //TODO реализовать метод остановки нити и ее удаление из списка monitors
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection urlConnection;

        while (!isInterrupted()) {
            try {
                url = new URL(host.getUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if (responseCode == 200) {
                    if (!host.isAvailable()) {
                        host.setAvailable(true);
                        Host.updateAvailable(host);
                        String text = host.getUrl() + "\n" + HelpText.getByCode("host_available");
                        new Message().sendMessage(user, text, bot);
                    }
                } else if (responseCode == 301 || responseCode == 302) {
                    url = new URL(urlConnection.getHeaderField("Location"));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    int newResponseCode = urlConnection.getResponseCode();
                    if (newResponseCode == 200) {
                        if (!host.isAvailable()) {
                            host.setAvailable(true);
                            Host.updateAvailable(host);
                            String text = host.getUrl() + "\n" + HelpText.getByCode("host_available");
                            new Message().sendMessage(user, text, bot);
                        }
                    }
                }
            } catch (IOException e) {
                if (host.isAvailable()) {
                    host.setAvailable(false);
                    Host.updateAvailable(host);
                    String text = host.getUrl() + "\n" + HelpText.getByCode("host_not_available");
                    new Message().sendMessage(user, text, bot);
                }
            }

            try {
                TimeUnit.MINUTES.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
