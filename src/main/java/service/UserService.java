package service;

import dao.UserDao;
import entity.User;

public class UserService {
    private UserDao userDao = new UserDao();

    public void add(User user) {
        userDao.add(user);
    }
}
