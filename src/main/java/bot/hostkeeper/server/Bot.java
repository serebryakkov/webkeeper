package bot.hostkeeper.server;

import bot.hostkeeper.config.SpringConfig;
import bot.hostkeeper.controller.Controller;
import bot.hostkeeper.entity.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Component
public class Bot extends TelegramLongPollingBot {

    @PostConstruct
    public void registerBot() {
        System.out.println("PostConstruct method called");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
            System.out.println("Бот создан и зарегистрирован");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    private final Controller controller = new Controller();

//    {
//        entity.Message.setBot(this);
//    }

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
                System.out.println("Вызвана команда 'Список сайтов'");
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
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}