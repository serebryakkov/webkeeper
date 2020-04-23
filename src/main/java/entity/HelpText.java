package entity;

import dao.HelpTextDao;

public class HelpText {
    private final String text;

    public HelpText(Code code) {
        this.text = new HelpTextDao().getByCode(code.code);
    }

    public void sendMessage(User user) {
        // TODO подумать как реализовать.
    }

    public static String getByCode(String code) {
        return new HelpTextDao().getByCode(code);
    }

    public enum Code {
        WELCOME("welcome");

        private final String code;

        Code(String code) {
            this.code = code;
        }
    }
}
