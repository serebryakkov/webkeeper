package server;

import controller.Controller;
import entity.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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
                processMessage(message);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            processCallbackQuery(callbackQuery);
        }
    }

    private void processMessage(Message message) {
        String text = message.getText();
        User user = new User();
        user.setUid(message.getChatId());
        user.setUsername(message.getChat().getUserName());

        switch (text) {
            case "/start":
                controller.startBot(user, this);
                break;
            case "Список сайтов":
                controller.getAndSendHostsList(user, this);
                break;
            case "О боте":
                controller.getAndSendAboutBotInfo(user, this);
                break;
            case "Отмена":
                user.setState(User.State.NULL);
                controller.cancelHostAdding(user, this);
            default:
                if (User.getUserState(user).equals(User.State.SITE_ADDING))
                    controller.hostAddAndSendMessage(user, text, this);
                break;
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        String callbackQueryData = callbackQuery.getData();
        User user = new User();
        user.setUid(callbackQuery.getFrom().getId());
        if (callbackQueryData.equals("add_host")) {
            user.setState(User.State.SITE_ADDING);
            controller.sendHostNameRequest(user, this);
        } else if (callbackQueryData.startsWith("site_")) {
            String hostId = callbackQueryData.substring(callbackQueryData.indexOf("_") + 1);
            controller.getAndSendHostInfo(user, hostId, this);
        } else if (callbackQueryData.startsWith("delete_host_")) {
            String hostId = callbackQueryData.substring(callbackQueryData.lastIndexOf("_") + 1);
            controller.deleteHostAndSendMessage(user, hostId, this);
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
