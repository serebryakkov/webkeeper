package service;

import dao.HostDao;
import entity.Host;
import entity.User;

import java.util.List;

public class HostService {
    private HostDao hostDao = new HostDao();

    public void add(User user, String url) {
        hostDao.add(user, url);
    }

    public Host getById(int id) {
        return hostDao.getById(id);
    }

    public List<Host> getAllByUserId(User user) {
        return hostDao.getAllByUserId(user);
    }

    public void remove(User user, int id) {
        hostDao.remove(user, id);
    }
}
