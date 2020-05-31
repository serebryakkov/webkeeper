package bot.hostkeeper.entity;

import bot.hostkeeper.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import bot.hostkeeper.server.Bot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private static final MessageDao messageDao = new MessageDao();

    @Autowired
    @Qualifier("bot.hostkeeper.server.Bot")
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

//    public static void setBot(Bot workBot) {
//        bot = workBot;
//    }

    public void sendMessage() {
        System.out.println("Вызван метод 'sendMessage'");
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.disableWebPagePreview();
        sendMessage.setChatId(user.getUid());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(createButtons());

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

    private ReplyKeyboard createButtons() {
        if (code == Code.WELCOME
                || code == Code.ABOUT_BOT
                || code == Code.SITE_ADDING_CANCEL
                || code == Code.HOST_SUCCESSFULLY_ADDED
                || code == Code.HOST_SUCCESSFULLY_DELETED)
            return createMainButtons();
        else if (code == Code.HOSTS_LIST)
            return createHostsListInlineButtons(user.getHosts());
        else if (code == Code.ENTER_HOST_NAME
                || code == Code.HOST_EXISTS
                || code == Code.INVALID_URL
                || code == Code.ADD_META_TAG
                || code == Code.META_TAG_NOT_FOUND)
            return createCheckAndCancelButtons();
        else if (code == Code.HOST_INFO) {
            return createDeleteHostInlineButton();
        }
        return null;
    }

    private ReplyKeyboardMarkup createMainButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Список сайтов"));
        keyboardFirstRow.add(new KeyboardButton("О боте"));

        keyboardRowList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup createHostsListInlineButtons(List<Host> hosts) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                buttons = new ArrayList<>();
                buttons.add(new InlineKeyboardButton().setText(host.getUrl().substring(7)).
                        setCallbackData("site_" + host.getId()));
                inlineKeyboardButtons.add(buttons);
            }
        }
        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Добавить сайт").setCallbackData("add_host"));
        inlineKeyboardButtons.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    private ReplyKeyboardMarkup createCheckAndCancelButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        if (code == Code.ENTER_HOST_NAME
                || code == Code.HOST_EXISTS
                || code == Code.INVALID_URL) {
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            keyboardFirstRow.add(new KeyboardButton("Отмена"));

            keyboardRowList.add(keyboardFirstRow);
        } else if (code == Code.ADD_META_TAG
                || code == Code.META_TAG_NOT_FOUND) {
            KeyboardRow keyboardFirstRow = new KeyboardRow();
            keyboardFirstRow.add(new KeyboardButton("Подтвердить"));
            KeyboardRow keyboardSecondRow = new KeyboardRow();
            keyboardSecondRow.add(new KeyboardButton("Отмена"));

            keyboardRowList.add(keyboardFirstRow);
            keyboardRowList.add(keyboardSecondRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup createDeleteHostInlineButton() {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;

        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Удалить сайт")
                .setCallbackData("delete_host_" + host.getId()));
        inlineKeyboardButtons.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);
        return inlineKeyboardMarkup;
    }

    public static String getHelpTextByCode(String code) {
        return messageDao.getByCode(code);
    }

    public enum Code {
        WELCOME("welcome"),
        ABOUT_BOT("about_bot"),
        HOSTS_LIST("hosts_list"),
        ENTER_HOST_NAME("enter_host_name"),
        SITE_ADDING_CANCEL("site_adding_cancel"),
        HOST_SUCCESSFULLY_ADDED("host_successfully_added"),
        HOST_SUCCESSFULLY_DELETED("host_successfully_deleted"),
        HOST_EXISTS("host_exists"),
        INVALID_URL("invalid_url"),
        HOST_INFO("host_info"),
        ADD_META_TAG("add_meta_tag"),
        META_TAG_NOT_FOUND("meta_tag_not_found");

        private final String code;

        Code(String code) {
            this.code = code;
        }
    }
}
