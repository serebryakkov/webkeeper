package service;

import dao.HelpTextDao;

public class HelpTextService {
    private HelpTextDao helpTextDao = new HelpTextDao();

    public String getByCode(String code) {
        return helpTextDao.getByCode(code);
    }
}
