package by.smirnov.currencyconverterbot.service.buttons;

import by.smirnov.currencyconverterbot.entity.Currencies;
import by.smirnov.currencyconverterbot.repository.CurrencyRepository;
import by.smirnov.currencyconverterbot.service.message.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.Constants.DELIM;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_CHOOSE_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.Constants.TARGET;

@Service
@RequiredArgsConstructor
public class ExchangeButtonsServiceImpl implements ExchangeButtonsService{

    private final CurrencyRepository currencyRepository;
    private final MessageSender messageSender;

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
        Currencies originalCurrency = currencyRepository.getOriginalCurrency(chatId);
        Currencies targetCurrency = currencyRepository.getTargetCurrency(chatId);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for (Currencies currency : Currencies.values()) {
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(originalCurrency, currency))
                                    .callbackData(ORIGINAL + DELIM + currency)
                                    .build(),
                            InlineKeyboardButton.builder()
                                    .text(getCurrencyButton(targetCurrency, currency))
                                    .callbackData(TARGET + DELIM + currency)
                                    .build()));
        }
        return buttons;
    }

    private String getCurrencyButton(Currencies saved, Currencies current) {
        return saved == current ? current + " ✅" : current.name();
    }
}
