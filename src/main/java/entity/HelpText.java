package entity;

import dao.HelpTextDao;

public class HelpText {
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static String getByCode(String code) {
        return new HelpTextDao().getByCode(code);
    }
}
