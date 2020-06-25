package bot.hostkeeper.entity;

import bot.hostkeeper.dao.HelpTextDao;
import bot.hostkeeper.util.KeyboardCreator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bot.hostkeeper.server.Bot;

import java.text.SimpleDateFormat;

public class Message {
    private static final HelpTextDao HELP_TEXT_DAO = new HelpTextDao();

    private static Bot bot;

    private final Code code;
    private final String text;
    private final User user;
    private final Host host;

    public Message(Code code, User user) {
        this.code = code;
        this.user = user;
        this.text = getText();
        this.host = null;
    }

    public Message(Code code, User user, Host host) {
        this.code = code;
        this.user = user;
        this.host = host;
        this.text = getText();
    }

    public Code getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public Host getHost() {
        return host;
    }

    public static void setBot(Bot workBot) {
        bot = workBot;
    }

    public void sendMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(user.getUid());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(KeyboardCreator.getReplyKeyboard(this));

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getText() {
        String text;

        if (code == Code.HOST_INFO) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String textAvailable = (host.isAvailable() ? "Доступен" : "Недоступен");
            String textLastTimeCheck = dateFormat.format(host.getLastTimeCheck());

            text = String.format(getHelpTextByCode(code.code), host.getUrl(),
                    textAvailable, textLastTimeCheck);
        } else if (code == Code.ADD_META_TAG) {
            text = String.format(getHelpTextByCode(code.code), host.hashCode());
        } else {
            text = getHelpTextByCode(code.code);
        }

        return text;
    }

    public static String getHelpTextByCode(String code) {
        return HELP_TEXT_DAO.getByCode(code);
    }

    public enum Code {
        WELCOME("welcome", "replyKeyboardMarkup"),
        ABOUT_BOT("about_bot", "replyKeyboardMarkup"),
        HOSTS_LIST("hosts_list", "inlineKeyboardMarkup"),
        ENTER_HOST_NAME("enter_host_name", "replyKeyboardMarkup"),
        SITE_ADDING_CANCEL("site_adding_cancel", "replyKeyboardMarkup"),
        HOST_SUCCESSFULLY_ADDED("host_successfully_added", "replyKeyboardMarkup"),
        HOST_SUCCESSFULLY_DELETED("host_successfully_deleted", "replyKeyboardMarkup"),
        HOST_EXISTS("host_exists", "replyKeyboardMarkup"),
        INVALID_URL("invalid_url", "replyKeyboardMarkup"),
        HOST_INFO("host_info", "inlineKeyboardMarkup"),
        ADD_META_TAG("add_meta_tag", "replyKeyboardMarkup"),
        META_TAG_NOT_FOUND("meta_tag_not_found", "replyKeyboardMarkup");

        private final String code;
        private final String keyboardMode;

        Code(String code, String keyboardMode) {
            this.code = code;
            this.keyboardMode = keyboardMode;
        }

        public String getKeyboardMode() {
            return keyboardMode;
        }
    }
}
