package controller;

import entity.HelpText;
import entity.Host;
import entity.Monitor;
import entity.User;
import ui.*;

import java.util.List;

public class Controller {
    // Метод добавляет пользователя в БД, если его там нет.
    public void startBot(User user) {
        User.add(user);
        String text = HelpText.getByCode("welcome");
        new Message().sendMessage(user, text);
    }

    /**
     * Метод получает список всех сайтов юзера и отправляет ему сообщение с ними.
     * @param user Объект типа User.
     */
    public void getAndSendHostsList(User user) {
        List<Host> hosts = Host.getAllByUserId(user);
        user.setHosts(hosts);
        String text = HelpText.getByCode("hosts_list");
        new HostsListMessage().sendMessage(user, text);
    }

    public void getAndSendAboutBotInfo(User user) {
        String text = HelpText.getByCode("about_bot");
        new Message().sendMessage(user, text);
    }

    public void sendHostNameRequest(User user) {
        User.updateUserState(user);
        String text = HelpText.getByCode("enter_host_name");
        new HostNameRequestMessage().sendMessage(user, text);
    }

    public void cancelHostAdding(User user) {
        User.updateUserState(user);
        String text = HelpText.getByCode("site_adding_cancel");
        new Message().sendMessage(user, text);
        getAndSendHostsList(user);
    }

    public void hostAddAndSendMessage(User user, String url) {
        // TODO реализовать проверку на существование такого хоста в списке юзера.
        Host host = new Host();
        host.setUrl(url);
        host.setUid(user.getId());
        if (Host.validateUrl(host)) {
            Host.add(host);
            new Monitor(host, user);
            user.setState(User.State.NULL);
            User.updateUserState(user);
            String text = HelpText.getByCode("host_successfully_added");
            new Message().sendMessage(user, text);
            getAndSendHostsList(user);
        } else {
            String text = HelpText.getByCode("invalid_url");
            new HostNameRequestMessage().sendMessage(user, text);
        }
    }

    public void getAndSendHostInfo(User user, String hostId) {
        Host host = Host.getById(Integer.parseInt(hostId));
        new HostInfoMessage().sendMessage(user, host);
    }

    public void deleteHostAndSendMessage(User user, String hostId) {
        Host.remove(user, Integer.parseInt(hostId));
        Monitor.stopAndRemoveMonitor(Host.getById(Integer.parseInt(hostId)));
        String text = HelpText.getByCode("host_successfully_deleted");
        new Message().sendMessage(user, text);
        getAndSendHostsList(user);
    }
}
