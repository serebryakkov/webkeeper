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

    // Метод отправляет пользователю сообщение со списком его сайтов.
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
        user.setState(User.State.SITE_ADDING);
        User.updateUserState(user);
        String text = HelpText.getByCode("enter_host_name");
        new HostNameRequestMessage().sendMessage(user, text);
    }

    // Метод обновляет значение свойства state у пользователя
    // и отправляет ему сообщение об отмене добавления сайта.
    public void cancelHostAdding(User user) {
        user.setState(User.State.NULL);
        User.updateUserState(user);
        String text = HelpText.getByCode("site_adding_cancel");
        new Message().sendMessage(user, text);
        getAndSendHostsList(user);
    }

    public void hostAddAndSendMessage(User user, String url) {
        Host host = new Host();
        host.setUrl(url);
        host.setUid(user.getId());
        if (Host.validateUrl(host)) {
            if (Host.exists(host, user)) {
                String text = HelpText.getByCode("host_exists");
                new HostNameRequestMessage().sendMessage(user, text);
            } else {
                Host.add(host);
                new Monitor(host, user);
                user.setState(User.State.NULL);
                User.updateUserState(user);
                String text = HelpText.getByCode("host_successfully_added");
                new Message().sendMessage(user, text);
                getAndSendHostsList(user);
            }
        } else {
            String text = HelpText.getByCode("invalid_url");
            new HostNameRequestMessage().sendMessage(user, text);
        }
    }

    // Метод отправляет пользователю сообщение с информацией о хосте.
    public void getAndSendHostInfo(User user, String hostId) {
        Host host = Host.getById(Integer.parseInt(hostId));
        new HostInfoMessage().sendMessage(user, host);
    }

    // Метод удаляет хост из списка пользователя,
    // а также останавливает нить для его мониторинга.
    // После чего отправляет сообщение о том что ресурс удален.
    public void deleteHostAndSendMessage(User user, String hostId) {
        Host host = Host.getById(Integer.parseInt(hostId));
        Host.remove(host);
        Monitor.stopAndRemoveMonitor(host);
        String text = HelpText.getByCode("host_successfully_deleted");
        new Message().sendMessage(user, text);
        getAndSendHostsList(user);
    }
}
