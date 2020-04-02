package server;

import controller.Controller;
import entity.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {
    private Controller controller = new Controller();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/start")) {
                    User user = new User();
                    user.setId(message.getChatId());
                    user.setUsername(message.getChat().getUserName());
                    controller.startBot(user, this);
                } else if (text.equals("Список сайтов")) {
                    System.out.println("Ветка условия 'Список сайтов'");
                    User user = new User();
                    user.setId(message.getChatId());
                    user.setUsername(message.getChat().getUserName());
                    controller.getAndSendHostsList(user, this);
                }
            }
        } else if (update.hasCallbackQuery()) {

        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
