package bot.hostkeeper.util;

import bot.hostkeeper.dao.CommandDao;
import bot.hostkeeper.entity.Host;
import bot.hostkeeper.entity.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class KeyboardCreator {

    private static final CommandDao COMMAND_DAO = new CommandDao();

    public static ReplyKeyboard getReplyKeyboard(Message message) {

        if (message.getCode().getKeyboardMode().equals("replyKeyboardMarkup"))
            return getReplyKeyboardMarkup(message);
        else if (message.getCode().getKeyboardMode().equals("inlineKeyboardMarkup"))
            return getInlineKeyboardMarkup(message);

        return null;
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(Message message) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (message.getCode() == Message.Code.WELCOME
            || message.getCode() == Message.Code.ABOUT_BOT
            || message.getCode() == Message.Code.SITE_ADDING_CANCEL
            || message.getCode() == Message.Code.HOST_SUCCESSFULLY_ADDED
            || message.getCode() == Message.Code.HOST_SUCCESSFULLY_DELETED)
            replyKeyboardMarkup.setKeyboard(getAboutBotAndSitesListButtons());
        else if (message.getCode() == Message.Code.ADD_META_TAG)
            replyKeyboardMarkup.setKeyboard(getCheckAndCancelButtons());
        else if (message.getCode() == Message.Code.ENTER_HOST_NAME
                    || message.getCode() == Message.Code.HOST_EXISTS
                    || message.getCode() == Message.Code.INVALID_URL)
            replyKeyboardMarkup.setKeyboard(getCancelButton());

        return replyKeyboardMarkup;
    }

    private static List<KeyboardRow> getAboutBotAndSitesListButtons() {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(COMMAND_DAO.getCommandTextByCode("hosts_list")));
        keyboardFirstRow.add(new KeyboardButton(COMMAND_DAO.getCommandTextByCode("about_bot")));

        keyboardRowList.add(keyboardFirstRow);

        return keyboardRowList;
    }

    private static List<KeyboardRow> getCheckAndCancelButtons() {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Подтвердить"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Отмена"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);

        return keyboardRowList;
    }

    private static List<KeyboardRow> getCancelButton() {
        List<KeyboardRow> keyboardRowList = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Отмена"));

        keyboardRowList.add(keyboardFirstRow);

        return keyboardRowList;
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup(Message message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        if (message.getCode() == Message.Code.HOSTS_LIST)
            inlineKeyboardMarkup.setKeyboard(getHostsListButtons(message));
        else if (message.getCode() == Message.Code.HOST_INFO)
            inlineKeyboardMarkup.setKeyboard(getHostInfoButtons(message));
        else if (message.getCode() == Message.Code.ASK_CHECK_INTERVAL)
            inlineKeyboardMarkup.setKeyboard(getCheckIntervalButtons(message));

        return inlineKeyboardMarkup;
    }

    private static List<List<InlineKeyboardButton>> getHostsListButtons(Message message) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;
        List<Host> hosts = message.getUser().getHosts();

        if (hosts.size() > 0) {
            for (Host host : hosts) {
                buttons = new ArrayList<>();
                buttons.add(new InlineKeyboardButton().setText(host.getUrl().substring(7)).
                        setCallbackData("site_" + host.getId()));
                inlineKeyboardButtons.add(buttons);
            }
        }
        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText(COMMAND_DAO.getCommandTextByCode("add_host"))
                .setCallbackData("add_host"));
        inlineKeyboardButtons.add(buttons);

        return inlineKeyboardButtons;
    }

    private static List<List<InlineKeyboardButton>> getHostInfoButtons(Message message) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> firstRowButtons = new ArrayList<>();
        firstRowButtons.add(new InlineKeyboardButton().setText(COMMAND_DAO.getCommandTextByCode("set_check_interval"))
                .setCallbackData("set_check_interval_" + message.getHost().getId()));

        List<InlineKeyboardButton> secondRowButtons = new ArrayList<>();
        secondRowButtons.add(new InlineKeyboardButton().setText(COMMAND_DAO.getCommandTextByCode("delete_host"))
                .setCallbackData("delete_host_" + message.getHost().getId()));

        inlineKeyboardButtons.add(firstRowButtons);
        inlineKeyboardButtons.add(secondRowButtons);

        return inlineKeyboardButtons;
    }

    private static List<List<InlineKeyboardButton>> getCheckIntervalButtons(Message message) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

        List<InlineKeyboardButton> firstRowButtons = new ArrayList<>();
        firstRowButtons.add(new InlineKeyboardButton().setText("5")
                .setCallbackData("check_interval_5_for_" + message.getHost().getId()));
        firstRowButtons.add(new InlineKeyboardButton().setText("10")
                .setCallbackData("check_interval_10_for_" + message.getHost().getId()));

        List<InlineKeyboardButton> secondRowButtons = new ArrayList<>();
        secondRowButtons.add(new InlineKeyboardButton().setText("20")
                .setCallbackData("check_interval_20_for_" + message.getHost().getId()));
        secondRowButtons.add(new InlineKeyboardButton().setText("30")
                .setCallbackData("check_interval_30_for_" + message.getHost().getId()));

        inlineKeyboardButtons.add(firstRowButtons);
        inlineKeyboardButtons.add(secondRowButtons);

        return inlineKeyboardButtons;
    }
}
