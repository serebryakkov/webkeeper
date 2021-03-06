package bot.hostkeeper.controller;

import bot.hostkeeper.entity.interfaces.Monitor;
import bot.hostkeeper.util.MetaTagInspector;
import bot.hostkeeper.entity.*;

import java.util.List;

public class Controller {
    private final Monitor monitor = new ScheduledExecutorServiceMonitor();
    // Метод добавляет пользователя в БД, если его там нет.
    // А также отправляет ему стартовое сообщение.
    public void startBot(User user) {
        User.add(user);
        new Message(Message.Code.WELCOME, user).sendMessage();
    }

    // Метод отправляет пользователю сообщение со списком его сайтов.
    public void getAndSendHostsList(User user) {
        List<Host> hosts = Host.getAllByUserId(user);
        user.setHosts(hosts);
        new Message(Message.Code.HOSTS_LIST, user).sendMessage();
    }

    // Метод отправляет пользователю сообщение с информацией о боте.
    public void getAndSendAboutBotInfo(User user) {
        new Message(Message.Code.ABOUT_BOT, user).sendMessage();
    }

    // Метод отправляет пользователю сообщение
    // с просьбой ввести адрес сайта и кнопкой для отмены.
    public void sendHostNameRequest(User user) {
        user.setState(User.State.SITE_ADDING);
        User.updateUserState(user);
        new Message(Message.Code.ENTER_HOST_NAME, user).sendMessage();
    }

    // Метод обновляет значение свойства state у пользователя
    // и отправляет ему сообщение об отмене добавления сайта.
    public void cancelHostAdding(User user) {
        user.setState(User.State.NULL);
        User.updateUserState(user);
        new Message(Message.Code.SITE_ADDING_CANCEL, user).sendMessage();
        getAndSendHostsList(user);
    }

    // Метод проверяет введенный пользователем URL на валидность,
    // на существование в списке пользователя и если все ок,
    // то отправляет пользователю сообщение с метатегом
    // который нужно установить на сайт.
    public void checkUrlAndSendMetaTag(User user, String url) {
        Host host = new Host();
        host.setUrl(url);
        host.setUid(user.getUid());
        if (Host.urlIsValid(host)) {
            if (!Host.exists(host, user)) {
                User.State userState = User.State.ADD_META_TAG;
                userState.setStateName("ADD_META_TAG_");
                userState.setStateName(userState.getStateName() + host.getUrl());
                user.setState(userState);
                User.updateUserState(user);
                new Message(Message.Code.ADD_META_TAG, user, host).sendMessage();
            } else {
                new Message(Message.Code.HOST_EXISTS, user).sendMessage();
            }
        } else {
            new Message(Message.Code.INVALID_URL, user).sendMessage();
        }
    }

    public void checkMetaTagAndAddHost(User user) {
        String userState = user.getState().getStateName();
        String url = userState.substring(13);
        if (new MetaTagInspector().checkMetaTag(url)) {
            Host host = new Host();
            host.setUrl(url);
            host.setUid(user.getUid());
            Host.add(host);
            monitor.startMonitor(host, user);
            user.setState(User.State.NULL);
            User.updateUserState(user);
            new Message(Message.Code.HOST_SUCCESSFULLY_ADDED, user).sendMessage();
            getAndSendHostsList(user);
        } else {
            new Message(Message.Code.META_TAG_NOT_FOUND, user).sendMessage();
        }
    }

    // Метод отправляет пользователю сообщение с информацией о хосте.
    public void getAndSendHostInfo(User user, String hostId) {
        Host host = Host.getById(Integer.parseInt(hostId));
        new Message(Message.Code.HOST_INFO, user, host).sendMessage();
    }

    // Метод удаляет хост из списка пользователя,
    // а также останавливает нить для его мониторинга.
    // После чего отправляет сообщение о том что ресурс удален.
    public void deleteHostAndSendMessage(User user, String hostId) {
        Host host = Host.getById(Integer.parseInt(hostId));
        Host.remove(host);
        monitor.stopMonitor(host);
        new Message(Message.Code.HOST_SUCCESSFULLY_DELETED, user).sendMessage();
        getAndSendHostsList(user);
    }

    public void askCheckInterval(User user, String hostId) {
        user.setState(User.State.SET_CHECK_INTERVAL);
        User.updateUserState(user);
        Host host = Host.getById(Integer.parseInt(hostId));
        new Message(Message.Code.ASK_CHECK_INTERVAL, user, host).sendMessage();
    }
}
