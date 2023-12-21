package by.smirnov.currencyconverterbot.components.buttons

import by.smirnov.currencyconverterbot.repository.QueryDateRepository
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

import java.time.LocalDate

import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES_NAME
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES_NAME_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES_NAME
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES_NAME_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.MessageConstants.DAILY_RATE_TYPE_MESSAGE

@Component
@TupleConstructor(includes = ['queryDateRepository'], includeFields = true, includeProperties = false, force = true)
class RateButtonsImpl implements RateButtons {

    @Autowired
    private final QueryDateRepository queryDateRepository

    @Override
    SendMessage getButtons(Long chatId, LocalDate date) {
        def message = new SendMessage()
        message.setChatId(chatId)
        message.setText(String.format(DAILY_RATE_TYPE_MESSAGE, date))

        def keybdMarkup = new InlineKeyboardMarkup()
        List<List<InlineKeyboardButton>> keybd = []

        keybd << getCommonButtonsRow()
        keybd << getDynamicButtonsRow()
        keybdMarkup.setKeyboard(keybd)
        message.setReplyMarkup(keybdMarkup)

        queryDateRepository.setDate(chatId, date)

        return message
    }

    private static List<InlineKeyboardButton> getCommonButtonsRow(){
        List<InlineKeyboardButton> buttonsRow = []

        buttonsRow << getButton(MAIN_CURRENCIES_NAME, MAIN_CURRENCIES)
        buttonsRow << getButton(ALL_CURRENCIES_NAME, ALL_CURRENCIES)

        return buttonsRow;
    }

    private static List<InlineKeyboardButton> getDynamicButtonsRow(){
        List<InlineKeyboardButton> buttonsRow = []

        buttonsRow << getButton(MAIN_CURRENCIES_NAME_DYNAMIC, MAIN_CURRENCIES_DYNAMIC)
        buttonsRow << getButton(ALL_CURRENCIES_NAME_DYNAMIC, ALL_CURRENCIES_DYNAMIC)

        return buttonsRow;
    }

    private static InlineKeyboardButton getButton(String text, String callBackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callBackData)
                .build()
    }
}
