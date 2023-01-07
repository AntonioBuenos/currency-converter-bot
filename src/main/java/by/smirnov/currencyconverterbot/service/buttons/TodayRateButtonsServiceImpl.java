package by.smirnov.currencyconverterbot.service.buttons;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class TodayRateButtonsServiceImpl implements TodayRateButtonsService {

    public static final String TODAY_MAIN_CURRENCIES = "today_main_currencies";
    public static final String TODAY_ALL_CURRENCIES = "today_all_currencies";
    public static final String MAIN_CURRENCIES_NAME = "Основные валюты";
    public static final String ALL_CURRENCIES_NAME = "Все валюты";
    public static final String TODAY_RATE_TYPE_MESSAGE =
            "Выберите курсы основных валют или курсы всех валют, устанавливаемых НБРБ:";

    @Override
    public SendMessage getButtons(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(TODAY_RATE_TYPE_MESSAGE);

        InlineKeyboardMarkup keybdMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keybd = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        var yesButton = getButton(MAIN_CURRENCIES_NAME, TODAY_MAIN_CURRENCIES);
        var noButton = getButton(ALL_CURRENCIES_NAME, TODAY_ALL_CURRENCIES);

        buttonsRow.add(yesButton);
        buttonsRow.add(noButton);

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
