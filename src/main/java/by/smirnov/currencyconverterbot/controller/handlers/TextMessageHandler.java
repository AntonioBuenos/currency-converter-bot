package by.smirnov.currencyconverterbot.controller.handlers;

import by.smirnov.currencyconverterbot.components.buttons.DailyRateButtons;
import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService;
import by.smirnov.currencyconverterbot.util.DateParser;
import by.smirnov.currencyconverterbot.util.DoubleParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.FORMAT_RATES_RESPONSE;

@Component
@RequiredArgsConstructor
public class TextMessageHandler {

    private final BotExecutor executor;
    private final MainCurrencyRepository mainCurrencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final DailyRateButtons dailyRateButtons;

    public void handleTextMessage(Message message) {
        long chatId = message.getChatId();

        Double value = DoubleParser.parseDouble(message.getText());
        if (value != null) {
            MainCurrencies original = mainCurrencyRepository.getOriginalCurrency(chatId);
            MainCurrencies target = mainCurrencyRepository.getTargetCurrency(chatId);
            Double converted = currencyConversionService.convert(original, target, value);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE, value, original, converted, target);
            executor.executeMessage(message, rateMessage);
        } else {
            executor.executeMessage(dailyRateButtons.getButtons(chatId, DateParser.parseDate(message.getText())));
        }
    }
}
