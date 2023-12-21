package by.smirnov.currencyconverterbot.repository

import by.smirnov.currencyconverterbot.entity.MainCurrencies
import org.springframework.stereotype.Service

@Service
class MapMainCurrencyRepository implements MainCurrencyRepository {

    private final HashMap<Long, MainCurrencies> originalCurrency = [:]
    private final HashMap<Long, MainCurrencies> targetCurrency = [:]

    @Override
    MainCurrencies getOriginalCurrency(long chatId) {
        originalCurrency.get(chatId, MainCurrencies.USD)
    }

    @Override
    MainCurrencies getTargetCurrency(long chatId) {
        targetCurrency.get(chatId, MainCurrencies.USD)
    }

    @Override
    void setOriginalCurrency(long chatId, MainCurrencies currency) {
        originalCurrency.chatId = currency
    }

    @Override
    void setTargetCurrency(long chatId, MainCurrencies currency) {
        targetCurrency.chatId = currency
    }
}
