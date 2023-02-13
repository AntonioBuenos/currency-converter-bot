package by.smirnov.currencyconverterbot.components.buttons;

import by.smirnov.currencyconverterbot.components.message.MessageSender;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.DELIM;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TARGET;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_CHOOSE_CURRENCIES;

@Component
@RequiredArgsConstructor
public class ExchangeButtonsImpl implements ExchangeButtons {

    private final MainCurrencyRepository mainCurrencyRepository;
    private final MessageSender messageSender;
    private static final String SIGN_CHOSEN = " ✅";

    public SendMessage getButtons (Message message){
        var buttons = getInlineKeyboard(message);
        return messageSender.sendMessage(message, MESSAGE_CHOOSE_CURRENCIES, buttons);
    }

    public EditMessageReplyMarkup getButtons (Message message, long chatId){
        var buttons = getInlineKeyboard(message);
        return EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    private List<List<InlineKeyboardButton>> getInlineKeyboard (Message message) {
        long chatId = message.getChatId();
        MainCurrencies originalCurrency = mainCurrencyRepository.getOriginalCurrency(chatId);
        MainCurrencies targetCurrency = mainCurrencyRepository.getTargetCurrency(chatId);
        return Arrays.stream(MainCurrencies.values())
                .map(currency -> Arrays.asList(
                        buildButton(ORIGINAL, originalCurrency, currency),
                        buildButton(TARGET, targetCurrency, currency)))
                .toList();
    }

    private String getCurrencyButton(MainCurrencies saved, MainCurrencies current) {
        return saved == current ? current + SIGN_CHOSEN : current.name();
    }

    private InlineKeyboardButton buildButton(String callBack, MainCurrencies curType, MainCurrencies current){
        return InlineKeyboardButton.builder()
                .text(getCurrencyButton(curType, current))
                .callbackData(callBack + DELIM + current)
                .build();
    }
}
