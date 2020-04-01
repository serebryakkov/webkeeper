package service;

import dao.HelpTextDao;

public class HelpTextService {
    private HelpTextDao helpTextDao = new HelpTextDao();

    public String getByCode(String code) {
        System.out.println("HelpTextService getByCode called");
        return helpTextDao.getByCode(code);
    }
}
