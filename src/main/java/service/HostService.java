package service;

import dao.HostDao;
import entity.User;

import java.util.Map;

public class HostService {
    private HostDao hostDao = new HostDao();

    public void add(User user, String url) {
        hostDao.add(user, url);
    }

    public Map<Integer, String> getAllByUserId(User user) {
        return hostDao.getAllByUserId(user.getId());
    }
}
