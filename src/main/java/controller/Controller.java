package controller;

import entity.User;
import server.Bot;
import service.HelpTextService;
import service.HostService;
import service.UserService;
import view.HostsListMessage;
import view.StartMessage;

import java.util.Map;

public class Controller {
    private UserService userService = new UserService();
    private HelpTextService helpTextService = new HelpTextService();
    private HostService hostService;

    private StartMessage startMessage = new StartMessage();
    private HostsListMessage hostsListMessage;

    /**
     * Метод добавляет юзера в БД если его там нет
     * и отправляет ему стартовое сообщение.
     * @param user Объект типа User.
     * @param bot Объект типа Bot для вызова метода execute при отправке сообщения.
     */
    public void startBot(User user, Bot bot) {
        userService.add(user);
        String text = helpTextService.getByCode("welcome");
        startMessage.sendMessage(user, text, bot);
    }

    public void getAndSendHostsList(User user, Bot bot) {
        hostService = new HostService();
        Map<Integer, String> hostsList = hostService.getAllByUserId(user);
        String text = helpTextService.getByCode("hosts_list");
        hostsListMessage.sendMessage(user, text, hostsList, bot);
    }
}
