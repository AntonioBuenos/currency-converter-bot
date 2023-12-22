package by.smirnov.currencyconverterbot.controller.handlers

import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons
import by.smirnov.currencyconverterbot.controller.BotExecutor
import by.smirnov.currencyconverterbot.entity.MainCurrencies
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository
import by.smirnov.currencyconverterbot.repository.QueryDateRepository
import by.smirnov.currencyconverterbot.service.rate.DailyRateService
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message

import java.time.LocalDate

import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ALL_CURRENCIES_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.CommonConstants.DELIM
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES
import static by.smirnov.currencyconverterbot.constants.CommonConstants.MAIN_CURRENCIES_DYNAMIC
import static by.smirnov.currencyconverterbot.constants.CommonConstants.ORIGINAL
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TARGET

@Component
@TupleConstructor(
        includes = ['dailyRateService', 'mainCurrencyRepository', 'executor', 'queryDateRepository', 'exchangeButtons'],
        includeFields = true, includeProperties = false, force = true
)
class CallbackHandler {

    @Autowired
    private final DailyRateService dailyRateService

    @Autowired
    private final MainCurrencyRepository mainCurrencyRepository

    @Autowired
    private final BotExecutor executor

    @Autowired
    private final QueryDateRepository queryDateRepository

    @Autowired
    private final ExchangeButtons exchangeButtons

    void handleCallback(CallbackQuery callbackQuery) {
        def message = callbackQuery.getMessage()
        def callbackData = callbackQuery.getData()
        def chatId = message.getChatId()
        def messageId = message.getMessageId()
        switch (callbackData) {
            case MAIN_CURRENCIES ->
                executor.editMessage(dailyRateService.getMainRates(getDate(chatId)), chatId, messageId)
            case ALL_CURRENCIES -> executor.editMessage(dailyRateService.getRates(getDate(chatId)), chatId, messageId)
            case MAIN_CURRENCIES_DYNAMIC -> executor.editMessage(dailyRateService.getMainRatesDynamic(getDate(chatId)), chatId, messageId)
            case ALL_CURRENCIES_DYNAMIC -> executor.editMessage(dailyRateService.getRatesDynamic(getDate(chatId)), chatId, messageId)
            default -> processConversion(message, callbackData, chatId)
        }
    }

    private LocalDate getDate(Long chatId) {
        queryDateRepository.getDate(chatId)
    }

    private void processConversion(Message message, String callbackData, long chatId) {
        String[] param = callbackData.split(DELIM)
        String action = param[0]
        def newCurrency = MainCurrencies.valueOf(param[1])
        if (action == ORIGINAL) mainCurrencyRepository.setOriginalCurrency(chatId, newCurrency)
        else if (action == TARGET) mainCurrencyRepository.setTargetCurrency(chatId, newCurrency)
        executor.executeMessage(exchangeButtons.getButtons(message, chatId))
    }
}
