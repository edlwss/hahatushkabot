package ru.edel.java.hahatushkabot.server;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.edel.java.hahatushkabot.model.JokesModel;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService {
    private final TelegramBot telegramBot;
    private final RestTemplate restTemplate;
    private static final String JOKES_SERVICE_URL = "http://localhost:8080/jokes";

    @Autowired
    private  VisitorsService visitorsService;

    private final Map<Long, String> userState = new ConcurrentHashMap<>();
//    private final Map<Long, Integer> userInputId = new ConcurrentHashMap<>();

    @Autowired
    public TelegramBotService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        this.restTemplate = new RestTemplate();
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::handleUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }

    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            Long chatId = update.message().chat().id();
            String messageText = update.message().text().trim();
            String state = userState.get(chatId);

            if (state != null) {
                handleUserState(chatId, messageText, state);
            } else {
                handleCommands(chatId, messageText);
            }
        }
    }

    private void handleUserState(Long chatId, String messageText, String state) {
        switch (state) {
//            case "ADD_JOKE":
//                appendJoke(chatId, messageText);
//                break;
//            case "UPDATE_JOKE":
//                updateJokeState(chatId, messageText);
//                break;
//            case "DELETE_JOKE":
//                deleteJoke(chatId, messageText);
//                break;
            case "GET_JOKE_BY_ID":
                getterJokeById(chatId, messageText);
                break;
        }
    }

    private void handleCommands(Long chatId, String messageText) {
        switch (messageText) {
            case "/start":
                sendStartMessage(chatId);
                break;
            case "/help":
                sendHelpMessage(chatId);
                break;
            case "Все хахатушки!":
                getterJokes(chatId);
                break;
            case "Любая хахатушка!":
                getterJokeRandom(chatId);
                break;
//            case "Новая хахатушка!":
//                requestJoke(chatId);
//                break;
//            case "Обновить хахатушку!":
//                requestUpdateJoke(chatId);
//                break;
//            case "Удалить хахатушку!":
//                requestDeleteJoke(chatId);
//                break;
            case "Хахатушка по ID!":
                requestGetJokeById(chatId);
                break;
            case "Топ 5 хахатушек!":
                getTopJokes(chatId);
                break;
            default:
                sendMessage(chatId, "Неизвестная команда. Введите /help для списка команд.");
                break;
        }
    }

    private void sendStartMessage(Long chatId) {
        String startMessage = "Бот готов принять команду!";
        SendMessage request = new SendMessage(chatId, startMessage)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyMarkup(createKeyboard());

        this.telegramBot.execute(request);
    }

    private ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Все хахатушки!", "Любая хахатушка!", "Хахатушка по ID!"},
//                new String[]{"Обновить хахатушку!", "Удалить хахатушку!", "Новая хахатушка!"},
                new String[]{"Топ 5 хахатушек!"}
        );
        replyKeyboardMarkup.resizeKeyboard(true);
        return replyKeyboardMarkup;
    }


    private void sendHelpMessage(Long chatId) {
        String helpMessage = "Доступные команды:\n" +
                "/start - Начать использование бота\n" +
                "/help - Список доступных команд\n" +
                "Все хахатушки! - Получить список всех хахатушек\n" +
                "Любая хахатушка! - Получить случайную хахатушку\n" +
//                "Новая хахатушка! - Добавить новую хахатушку\n" +
//                "Обновить хахатушку! - Обновить существующую хахатушку\n" +
//                "Удалить хахатушку! - Удалить хахатушку по ID\n" +
                "Хахатушка по ID! - Получить хахатушку по ID"+
                "Топ 5 хахатушек! - Получить 5 самых популярных хахатушек";
        sendMessage(chatId, helpMessage);
    }

    private void getterJokes(Long chatId) {
        JokesModel[] jokes = restTemplate.getForObject(JOKES_SERVICE_URL, JokesModel[].class);

        StringBuilder messageText = new StringBuilder("Хахатушек слишком много! Но вы можете полюбоваться 15 хахатушками :)\n\n");
        for (JokesModel joke: jokes) {
            messageText.append(joke.getJok()).append("\n");
        }
        sendMessage(chatId, messageText.toString());
    }

    private void getTopJokes(Long chatId){
        List<String> topJokes = restTemplate.getForObject(JOKES_SERVICE_URL + "/top", List.class);
        StringBuilder messageText = new StringBuilder("Топ 5 самых пополярных хахатушек!\n\n");
        for (String joke: topJokes) {
            messageText.append(joke).append("\n");
        }
        sendMessage(chatId, messageText.toString());
    }

    private void getterJokeRandom(Long chatId) {
        JokesModel joke = restTemplate.getForObject(JOKES_SERVICE_URL + "/random", JokesModel.class);

        String jokeMessage = "Пользователь " + chatId + " захотел хахатушку!\n\n" +
                "Вот, пожалуйста, хихикайте:\n" + joke.getJok() + "\n\n" +
                "Отправлено " + new Date();
        sendMessage(chatId, jokeMessage);
        visitorsService.addVisitors(chatId, "joke random", joke);
    }

//    private void appendJoke(Long chatId, String jokeText) {
//        JokesModel jokesModel = new JokesModel();
//        jokesModel.setJok(jokeText);
//
//        restTemplate.postForObject(JOKES_SERVICE_URL, jokesModel, String.class);
//        sendMessage(chatId, "Уважаемый, хахатушка добавлена!");
//        userState.remove(chatId);
//    }
//
//    private void updateJoke(Long chatId, String jokeText) {
//        Integer jokeId = userInputId.get(chatId);
//        JokesModel jokesModel = new JokesModel();
//        jokesModel.setJok(jokeText);
//
//        restTemplate.put(JOKES_SERVICE_URL + "/" + jokeId, jokesModel, String.class);
//        sendMessage(chatId, "Хахатушка с ID " + jokeId + " обновлена!");
//        userState.remove(chatId);
//        userInputId.remove(chatId);
//
//    }
//
//    private void deleteJoke(Long chatId, String messageText) {
//        int jokeId = Integer.parseInt(messageText);
//        restTemplate.delete(JOKES_SERVICE_URL + "/" + jokeId, String.class);
//        sendMessage(chatId, "Хахатушка с ID " + jokeId + " удалена!");
//
//        userState.remove(chatId);
//    }

    private void getterJokeById(Long chatId, String messageText) {

        int jokeId = Integer.parseInt(messageText);
        JokesModel joke = restTemplate.getForObject(JOKES_SERVICE_URL + "/" + jokeId, JokesModel.class);
        String jokeMessage = "Хахатушка с ID " + jokeId + ":\n" + joke.getJok();
        sendMessage(chatId, jokeMessage);
        userState.remove(chatId);

        visitorsService.addVisitors(chatId, "joke getID", joke);
        restTemplate.postForObject(JOKES_SERVICE_URL, joke, String.class);
    }


//    private void requestJoke(Long chatId) {
//        sendMessage(chatId, "Введите текст хахатушки!");
//        userState.put(chatId, "ADD_JOKE");
//    }
//
//    private void requestUpdateJoke(Long chatId) {
//        sendMessage(chatId, "Введите ID хахатушки!");
//        userState.put(chatId, "UPDATE_JOKE");
//    }
//    private void updateJokeState(Long chatId, String messageText) {
//            if (userInputId.containsKey(chatId)) {
//                updateJoke(chatId, messageText);
//            } else {
//                int jokeId = Integer.parseInt(messageText);
//                userInputId.put(chatId, jokeId);
//                sendMessage(chatId, "Введите текст для обновления хахатушки с ID: " + jokeId);
//            }
//    }
//
//    private void requestDeleteJoke(Long chatId) {
//        sendMessage(chatId, "Введите ID хахатушки, которую хотите удалить!");
//        userState.put(chatId, "DELETE_JOKE");
//    }

    private void requestGetJokeById(Long chatId) {
        sendMessage(chatId, "Введите ID хахатушки, которую хотите получить!");
        userState.put(chatId, "GET_JOKE_BY_ID");
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage request = new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        this.telegramBot.execute(request);
    }
}