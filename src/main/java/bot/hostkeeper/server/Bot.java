package bot.hostkeeper.server;

import bot.hostkeeper.controller.Controller;
import bot.hostkeeper.entity.User;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Bot extends TelegramLongPollingBot {

    private final Controller controller = new Controller();

    {
        bot.hostkeeper.entity.Message.setBot(this);
    }

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
                controller.startBot(user);
                break;
            case "Список сайтов":
                controller.getAndSendHostsList(user);
                break;
            case "О боте":
                controller.getAndSendAboutBotInfo(user);
                break;
            case "Подтвердить":
                user.setState(User.getUserState(user));
                if (user.getState().equals(User.State.ADD_META_TAG))
                    controller.checkMetaTagAndAddHost(user);
                break;
            case "Отмена":
                controller.cancelHostAdding(user);
                break;
            case "/get_active_streams":
                for (Thread t : Thread.getAllStackTraces().keySet())
                    System.out.println(t.getName());
                break;
            default:
                if (User.getUserState(user).equals(User.State.SITE_ADDING))
                    controller.checkUrlAndSendMetaTag(user, text);
                break;
        }
    }

    private void processCallbackQuery(CallbackQuery callbackQuery) {
        String callbackQueryData = callbackQuery.getData();
        User user = new User();
        user.setUid(callbackQuery.getFrom().getId());

        if (callbackQueryData.equals("add_host")) {
            controller.sendHostNameRequest(user);
        } else if (callbackQueryData.startsWith("site_")) {
            String hostId = callbackQueryData.substring(callbackQueryData.indexOf("_") + 1);
            controller.getAndSendHostInfo(user, hostId);
        } else if (callbackQueryData.startsWith("delete_host_")) {
            String hostId = callbackQueryData.substring(callbackQueryData.lastIndexOf("_") + 1);
            controller.deleteHostAndSendMessage(user, hostId);
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
