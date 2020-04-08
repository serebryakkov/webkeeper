package ui;

import entity.Host;
import entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import server.Bot;

import java.util.ArrayList;
import java.util.List;

public class HostsListMessage {
    public void sendMessage(User user, String text, Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        sendMessage.setText(text);
        setInlineButtons(sendMessage, user.getHosts());

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setInlineButtons(SendMessage sendMessage, List<Host> hosts) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                buttons = new ArrayList<>();
                buttons.add(new InlineKeyboardButton().setText(host.getUrl()).
                        setCallbackData("site_" + host.getId()));
                inlineKeyboardButtons.add(buttons);
            }
        }
        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Добавить сайт").setCallbackData("add_host"));
        inlineKeyboardButtons.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
