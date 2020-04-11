package entity;

import dao.HelpTextDao;

public class HelpText {
    public static String getByCode(String code) {
        return new HelpTextDao().getByCode(code);
    }
}
