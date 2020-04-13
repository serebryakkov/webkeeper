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
    private static List<Monitor> monitors = new ArrayList<>();

    static {
        List<Host> hosts = Host.getAll();
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                new Monitor(host);
            }
        }
    }

    private Host host;
    private User user;
    private Bot bot;

    public Monitor(Host host) {
        this.host = host;
        monitors.add(this);
        this.start();
    }

    public Monitor(Host host, User user, Bot bot) {
        this.host = host;
        this.user = user;
        this.bot = bot;
        monitors.add(this);
        this.start();
    }

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL(host.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        while (!isInterrupted()) {
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
                    HttpURLConnection newConnection = (HttpURLConnection) url.openConnection();
                    int newResponseCode = newConnection.getResponseCode();
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
