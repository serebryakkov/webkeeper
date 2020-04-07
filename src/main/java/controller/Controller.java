package controller;

import entity.Host;
import entity.User;
import server.Bot;
import service.HelpTextService;
import service.HostService;
import service.UserService;
import view.*;

import java.util.Map;

public class Controller {
    private UserService userService = new UserService();
    private HelpTextService helpTextService = new HelpTextService();
    private HostService hostService = new HostService();

    private Message message = new Message();
    private HostsListMessage hostsListMessage = new HostsListMessage();
    private HostNameRequestMessage hostNameRequestMessage = new HostNameRequestMessage();
    private HostInfoMessage hostInfoMessage = new HostInfoMessage();

    /**
     * Метод добавляет юзера в БД если его там нет
     * и отправляет ему стартовое сообщение.
     * @param user Объект типа User.
     * @param bot Объект типа Bot для вызова метода execute при отправке сообщения.
     */
    public void startBot(User user, Bot bot) {
        userService.add(user);
        String text = helpTextService.getByCode("welcome");
        message.sendMessage(user, text, bot);
    }

    /**
     * Метод получает список всех сайтов юзера и отправляет ему сообщение с ними.
     * @param user Объект типа User.
     * @param bot Объект типа Bot для вызова метода execute при отправке сообщения.
     */
    public void getAndSendHostsList(User user, Bot bot) {
        Map<Integer, String> hostsList = hostService.getAllByUserId(user);
        String text = helpTextService.getByCode("hosts_list");
        hostsListMessage.sendMessage(user, text, hostsList, bot);
    }

    public void getAndSendAboutBotInfo(User user, Bot bot) {
        String text = helpTextService.getByCode("about_bot");
        message.sendMessage(user, text, bot);
    }

    public void sendHostNameRequest(User user, Bot bot) {
        userService.updateUserState(user);
        String text = helpTextService.getByCode("enter_host_name");
        hostNameRequestMessage.sendMessage(user, text, bot);
    }

    public void cancelHostAdding(User user, Bot bot) {
        userService.updateUserState(user);
        String text = helpTextService.getByCode("site_adding_cancel");
        message.sendMessage(user, text, bot);
        getAndSendHostsList(user, bot);
    }

    public void hostAddAndSendMessage(User user, String url, Bot bot) {
        hostService.add(user, url);
        String text = helpTextService.getByCode("host_successfully_added");
        message.sendMessage(user, text, bot);
        getAndSendHostsList(user, bot);
    }

    public void getAndSendHostInfo(User user, String hostId, Bot bot) {
        Host host = hostService.getById(Integer.parseInt(hostId));
        hostInfoMessage.sendMessage(user, host, bot);
    }
}
