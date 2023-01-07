package by.smirnov.currencyconverterbot.service.buttons;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodayRateButtonsServiceImpl implements TodayRateButtonsService{

    public static final String TODAY_MAIN_CURRENCIES = "today_main_currencies";
    public static final String TODAY_ALL_CURRENCIES = "today_all_currencies";

    @Override
    public SendMessage getButtons(Long chatId) {
        SendMessage message = new SendMessage(); //создаем объект отправки сообщений
        message.setChatId(String.valueOf(chatId)); //определяем ID чата
        message.setText("Выберите курсы основных валют или курсы всех валют, устанавливаемых НБРБ:"); //и сообщение к отправке

        InlineKeyboardMarkup keybdMarkup = new InlineKeyboardMarkup(); //создаем объект клавиатуры, принадлежащий сообщению
        List<List<InlineKeyboardButton>> keybd = new ArrayList<>(); //аргумент клавиатуры требует списка списков кнопок
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>(); //создаем список для кнопок (ряд кнопок)

        var yesButton = new InlineKeyboardButton(); //создаем кнопку
        yesButton.setText("Основные валюты"); //надпись на кнопке
        yesButton.setCallbackData(TODAY_MAIN_CURRENCIES); //значение, которое будет возвращать кнопка при нажатии

        var noButton = new InlineKeyboardButton();
        noButton.setText("Все валюты");
        noButton.setCallbackData(TODAY_ALL_CURRENCIES);

        buttonsRow.add(yesButton); //добавляем кнопки в ряд (список кнопок)
        buttonsRow.add(noButton);

        keybd.add(buttonsRow); //добавляем ряд кнопок в клавиатуру

        keybdMarkup.setKeyboard(keybd); //передаем сформированную клавиатуру в объект клавиатуры сообщения
        message.setReplyMarkup(keybdMarkup); //устанавливаем сообщению объект его клавиатуры

        return message;
    }
}
