package controller;

import entity.User;
import server.Bot;
import service.HelpTextService;
import service.UserService;
import view.Message;

public class Controller {
    private UserService userService = new UserService();
    private HelpTextService helpTextService = new HelpTextService();

    private Message message = new Message();

    public void startBot(User user, Bot bot) {
        userService.add(user);
        String text = helpTextService.getByCode("welcome");
        message.sendMessage(user, text, bot);
    }
}
