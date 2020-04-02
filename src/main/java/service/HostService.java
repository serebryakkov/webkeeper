package service;

import dao.HostDao;
import entity.User;

import java.util.Map;

public class HostService {
    private HostDao hostDao = new HostDao();

    public Map<Integer, String> getAllByUserId(User user) {
        System.out.println("Метод getAllByUserId (HostService) вызван");
        return hostDao.getAllByUserId(user.getId());
    }
}
