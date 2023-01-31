package by.smirnov.currencyconverterbot.controller.handlers;

import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons;
import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository;
import by.smirnov.currencyconverterbot.repository.QueryDateRepository;
import by.smirnov.currencyconverterbot.service.rate.DailyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.DELIM;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TARGET;

@Component
@RequiredArgsConstructor
public class CallbackHandler {

    private final DailyRateService dailyRateService;
    private final MainCurrencyRepository mainCurrencyRepository;
    private final BotExecutor executor;
    private final QueryDateRepository queryDateRepository;
    private final ExchangeButtons exchangeButtons;

    public void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        switch (callbackData) {
            case MAIN_CURRENCIES ->
                    executor.editMessage(dailyRateService.getMainRates(getDate(chatId)), chatId, messageId);
            case ALL_CURRENCIES -> executor.editMessage(dailyRateService.getRates(getDate(chatId)), chatId, messageId);
            default -> processConversion(message, callbackData, chatId);
        }
    }

    private LocalDate getDate(Long chatId) {
        return queryDateRepository.getDate(chatId);
    }

    private void processConversion(Message message, String callbackData, long chatId) {
        String[] param = callbackData.split(DELIM);
        String action = param[0];
        MainCurrencies newCurrency = MainCurrencies.valueOf(param[1]);
        if (action.equals(ORIGINAL)) mainCurrencyRepository.setOriginalCurrency(chatId, newCurrency);
        else if (action.equals(TARGET)) mainCurrencyRepository.setTargetCurrency(chatId, newCurrency);
        executor.executeMessage(exchangeButtons.getButtons(message, chatId));
    }
}
