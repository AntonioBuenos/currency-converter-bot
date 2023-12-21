package by.smirnov.currencyconverterbot.components.buttons

import by.smirnov.currencyconverterbot.components.message.MessageSender
import by.smirnov.currencyconverterbot.entity.MainCurrencies
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton

import static by.smirnov.currencyconverterbot.constants.CommonConstants.DELIM
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ORIGINAL
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TARGET
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_CHOOSE_CURRENCIES

@Component
@TupleConstructor(includes = ['mainCurrencyRepository', 'messageSender'], includeFields = true, includeProperties = false, force = true)
class ExchangeButtonsImpl implements ExchangeButtons {

    @Autowired
    private final MainCurrencyRepository mainCurrencyRepository

    @Autowired
    private final MessageSender messageSender

    private static final String SIGN_CHOSEN = " ✅"

    SendMessage getButtons (Message message){
        def buttons = getInlineKeyboard(message)
        messageSender.sendMessage(message, MESSAGE_CHOOSE_CURRENCIES, buttons)
    }

    EditMessageReplyMarkup getButtons (Message message, long chatId){
        def buttons = getInlineKeyboard(message);
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build()
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboard (Message message) {
        def chatId = message.getChatId()
        def originalCurrency = mainCurrencyRepository.getOriginalCurrency(chatId)
        def targetCurrency = mainCurrencyRepository.getTargetCurrency(chatId)
        return Arrays.stream(MainCurrencies.values())
                .map{
                    Arrays.asList(
                        buildButton(ORIGINAL, originalCurrency, it),
                        buildButton(TARGET, targetCurrency, it)
                )}
                .toList()
    }

    private String getCurrencyButton(MainCurrencies saved, MainCurrencies current) {
        saved == current ? current + SIGN_CHOSEN : current.name()
    }

    private InlineKeyboardButton buildButton(String callBack, MainCurrencies curType, MainCurrencies current){
        return InlineKeyboardButton.builder()
                .text(getCurrencyButton(curType, current))
                .callbackData(callBack + DELIM + current)
                .build()
    }
}
