package view;

import entity.Host;
import entity.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import server.Bot;

import java.util.ArrayList;
import java.util.List;

public class HostInfoMessage {
    public void sendMessage(User user, Host host, Bot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        String text = host.getUrl() + "\n" + (host.isAvailable() ? "Доступен" : "Недоступен");
        sendMessage.setText(text);
        setInlineButtons(sendMessage, host);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setInlineButtons(SendMessage sendMessage, Host host) {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttons;

        buttons = new ArrayList<>();
        buttons.add(new InlineKeyboardButton().setText("Удалить сайт")
                .setCallbackData("delete_host_" + host.getId()));
        inlineKeyboardButtons.add(buttons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboardButtons);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
