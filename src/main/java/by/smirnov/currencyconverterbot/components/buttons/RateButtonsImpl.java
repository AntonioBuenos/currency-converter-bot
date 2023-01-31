package by.smirnov.currencyconverterbot.components.buttons;

import by.smirnov.currencyconverterbot.repository.QueryDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES_NAME;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES_NAME;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.DAILY_RATE_TYPE_MESSAGE;

@Component
@RequiredArgsConstructor
public class RateButtonsImpl implements RateButtons {

    private final QueryDateRepository queryDateRepository;

    @Override
    public SendMessage getButtons(Long chatId, LocalDate date) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(String.format(DAILY_RATE_TYPE_MESSAGE, date));

        InlineKeyboardMarkup keybdMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keybd = new ArrayList<>();

        keybd.add(getButtonsRow());
        keybdMarkup.setKeyboard(keybd);
        message.setReplyMarkup(keybdMarkup);

        queryDateRepository.setDate(chatId, date);

        return message;
    }

    private List<InlineKeyboardButton> getButtonsRow(){
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        buttonsRow.add(getButton(MAIN_CURRENCIES_NAME, MAIN_CURRENCIES));
        buttonsRow.add(getButton(ALL_CURRENCIES_NAME, ALL_CURRENCIES));

        return buttonsRow;
    }

    private InlineKeyboardButton getButton(String text, String callBackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBackData)
                .build();
    }
}
