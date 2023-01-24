package by.smirnov.currencyconverterbot.controller.handlers;

import by.smirnov.currencyconverterbot.components.buttons.RateButtons;
import by.smirnov.currencyconverterbot.components.commands.Commands;
import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.ActualCommandRepository;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService;
import by.smirnov.currencyconverterbot.util.DateParser;
import by.smirnov.currencyconverterbot.util.DoubleParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_STUB_REPLY;

@Component
@RequiredArgsConstructor
public class TextMessageHandler {

    private final BotExecutor executor;
    private final MainCurrencyRepository mainCurrencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final RateButtons rateButtons;
    private final ActualCommandRepository commandRepository;

    public void handleTextMessage(Message message) {
        long chatId = message.getChatId();

        Commands command = commandRepository.getActualCommand(chatId);
        String defautReply = String.format(MESSAGE_STUB_REPLY, message.getChat().getFirstName());
        if (command == null) {
            executor.executeMessage(message, defautReply);
            return;
        }

        switch (command) {
            case SET_CURRENCY -> handleConvertable(chatId, message);
            case RATES_BY_DATE ->
                    executor.executeMessage(rateButtons.getButtons(chatId, DateParser.parseDate(message.getText())));
            default -> executor.executeMessage(message, defautReply);
        }
    }

    private void handleConvertable(long chatId, Message message){
        Double value = DoubleParser.parseDouble(message.getText());
        if (value != null) {
            MainCurrencies original = mainCurrencyRepository.getOriginalCurrency(chatId);
            MainCurrencies target = mainCurrencyRepository.getTargetCurrency(chatId);
            Double converted = currencyConversionService.convert(original, target, value);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE, value, original, converted, target);
            executor.executeMessage(message, rateMessage);
        }
    }
}
