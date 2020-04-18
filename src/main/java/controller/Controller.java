package controller;

import entity.HelpText;
import entity.Host;
import entity.Monitor;
import entity.User;
import server.Bot;
import ui.*;

import java.util.List;

public class Controller {
    /**
     * Метод добавляет юзера в БД если его там нет
     * и отправляет ему стартовое сообщение.
     * @param user Объект типа User.
     * @param bot Объект типа Bot для вызова метода execute при отправке сообщения.
     */
    public void startBot(User user, Bot bot) {
        User.add(user);
        String text = HelpText.getByCode("welcome");
        new Message().sendMessage(user, text, bot);
    }

    /**
     * Метод получает список всех сайтов юзера и отправляет ему сообщение с ними.
     * @param user Объект типа User.
     * @param bot Объект типа Bot для вызова метода execute при отправке сообщения.
     */
    public void getAndSendHostsList(User user, Bot bot) {
        List<Host> hosts = Host.getAllByUserId(user);
        user.setHosts(hosts);
        String text = HelpText.getByCode("hosts_list");
        new HostsListMessage().sendMessage(user, text, bot);
    }

    public void getAndSendAboutBotInfo(User user, Bot bot) {
        String text = HelpText.getByCode("about_bot");
        new Message().sendMessage(user, text, bot);
    }

    public void sendHostNameRequest(User user, Bot bot) {
        User.updateUserState(user);
        String text = HelpText.getByCode("enter_host_name");
        new HostNameRequestMessage().sendMessage(user, text, bot);
    }

    public void cancelHostAdding(User user, Bot bot) {
        User.updateUserState(user);
        String text = HelpText.getByCode("site_adding_cancel");
        new Message().sendMessage(user, text, bot);
        getAndSendHostsList(user, bot);
    }

    public void hostAddAndSendMessage(User user, String url, Bot bot) {
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
            new Message().sendMessage(user, text, bot);
            getAndSendHostsList(user, bot);
        } else {
            String text = HelpText.getByCode("invalid_url");
            new HostNameRequestMessage().sendMessage(user, text, bot);
        }
    }

    public void getAndSendHostInfo(User user, String hostId, Bot bot) {
        Host host = Host.getById(Integer.parseInt(hostId));
        new HostInfoMessage().sendMessage(user, host, bot);
    }

    public void deleteHostAndSendMessage(User user, String hostId, Bot bot) {
        Host.remove(user, Integer.parseInt(hostId));
        Monitor.stopAndRemoveMonitor(Host.getById(Integer.parseInt(hostId)));
        String text = HelpText.getByCode("host_successfully_deleted");
        new Message().sendMessage(user, text, bot);
        getAndSendHostsList(user, bot);
    }
}
