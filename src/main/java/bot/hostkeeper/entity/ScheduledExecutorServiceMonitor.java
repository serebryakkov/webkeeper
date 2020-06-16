package bot.hostkeeper.entity;

import bot.hostkeeper.util.MetaTagInspector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceMonitor implements Monitor {

    private static final List<MonitorTask> tasksList = new ArrayList<>();
    private static final ScheduledExecutorService executorService =
            Executors.newScheduledThreadPool(2);

    {
        List<Host> hosts = Host.getAll();
        if (!hosts.isEmpty()) {
            for (Host host : hosts) {
                User user = new User();
                user.setUid(host.getUid());
                startMonitor(host, user);
            }
        }
    }

    @Override
    public void startMonitor(Host host, User user) {
        MonitorTask task = new MonitorTask(host, user);
        tasksList.add(task);
        task.setFuture(executorService.scheduleWithFixedDelay(task,
                                                            0,
                                                            30,
                                                            TimeUnit.MINUTES));
    }

    @Override
    public void stopMonitor(Host host) {
        Iterator<MonitorTask> iterator = tasksList.iterator();
        while (iterator.hasNext()) {
            MonitorTask task = iterator.next();
            if (task.host.equals(host)) {
                iterator.remove();
                System.out.println("Мониторинг ресурса " + task.host.getUrl() + " прекращен");
                task.getFuture().cancel(true);
            }
        }
    }

    private static class MonitorTask implements Runnable {
        private final Host host;
        private final User user;
        private ScheduledFuture<?> future;

        private MonitorTask(Host host, User user) {
            this.host = host;
            this.user = user;
        }

        public ScheduledFuture<?> getFuture() {
            return future;
        }

        public void setFuture(ScheduledFuture<?> future) {
            this.future = future;
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
            checkHost(host.getUrl());
        }
    }
}
