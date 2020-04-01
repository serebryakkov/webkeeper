package view;

import entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import server.Bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HostsListMessage {
    public void sendMessage(User user, String text, Map<Integer, String> hostsList, Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        sendMessage.setText(text);
        setInlineButtons(sendMessage, hostsList);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setInlineButtons(SendMessage sendMessage, Map<Integer, String> hostsList) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;
        if (hostsList.size() > 0) {
            for (Map.Entry<Integer, String> pair : hostsList.entrySet()) {
                buttons = new ArrayList<>();
                buttons.add(new InlineKeyboardButton().setText(pair.getValue()).
                        setCallbackData("site_" + pair.getKey()));
                inlineKeyboardButtons.add(buttons);
            }
        }
        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Добавить сайт").setCallbackData("add_site"));
        inlineKeyboardButtons.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
