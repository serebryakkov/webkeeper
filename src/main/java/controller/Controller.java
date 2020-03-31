package controller;

import entity.User;
import service.UserService;

public class Controller {
    private UserService userService = new UserService();

    public void startBot(User user) {
        userService.add(user);
    }
}
