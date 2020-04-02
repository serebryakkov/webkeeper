package service;

import dao.HelpTextDao;

public class HelpTextService {
    private HelpTextDao helpTextDao = new HelpTextDao();

    public String getByCode(String code) {
        System.out.println("Метод getByCode (HelpTextService) вызван");
        return helpTextDao.getByCode(code);
    }
}
