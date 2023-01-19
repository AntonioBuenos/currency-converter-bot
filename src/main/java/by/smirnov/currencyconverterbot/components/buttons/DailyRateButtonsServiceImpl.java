package by.smirnov.currencyconverterbot.components.buttons;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.Constants.TODAY;
import static by.smirnov.currencyconverterbot.constants.Constants.TOMORROW;

@Component
public class DailyRateButtonsServiceImpl implements DailyRateButtonsService {

    public static final String MAIN_CURRENCIES = "main_currencies";
    public static final String TODAY_MAIN_CURRENCIES = "today_main_currencies";
    public static final String TOMORROW_MAIN_CURRENCIES = "tomorrow_main_currencies";
    public static final String ALL_CURRENCIES = "all_currencies";
    public static final String TODAY_ALL_CURRENCIES = "today_all_currencies";
    public static final String TOMORROW_ALL_CURRENCIES = "tomorrow_all_currencies";
    public static final String MAIN_CURRENCIES_NAME = "Основные валюты";
    public static final String ALL_CURRENCIES_NAME = "Все валюты";
    public static final String DAYLY_RATE_TYPE_MESSAGE =
            "Выберите курсы основных валют или курсы всех валют, устанавливаемых НБРБ:";

    @Override
    public SendMessage getButtons(Long chatId, LocalDate date) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(DAYLY_RATE_TYPE_MESSAGE);

        InlineKeyboardMarkup keybdMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keybd = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        InlineKeyboardButton mainButton = null;
        InlineKeyboardButton allButton = null;
        if (date == TODAY) {
            mainButton = getButton(MAIN_CURRENCIES_NAME, TODAY_MAIN_CURRENCIES);
            allButton = getButton(ALL_CURRENCIES_NAME, TODAY_ALL_CURRENCIES);
        } else if (date == TOMORROW) {
            mainButton = getButton(MAIN_CURRENCIES_NAME, TOMORROW_MAIN_CURRENCIES);
            allButton = getButton(ALL_CURRENCIES_NAME, TOMORROW_ALL_CURRENCIES);
        }
        else {
            mainButton = getButton(MAIN_CURRENCIES_NAME, MAIN_CURRENCIES);
            allButton = getButton(ALL_CURRENCIES_NAME, ALL_CURRENCIES);
        }

        buttonsRow.add(mainButton);
        buttonsRow.add(allButton);

        keybd.add(buttonsRow);
        keybdMarkup.setKeyboard(keybd);
        message.setReplyMarkup(keybdMarkup);

        return message;
    }

    private InlineKeyboardButton getButton(String text, String callBackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBackData)
                .build();
    }
}
