package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapMainCurrencyRepositoryImpl implements MainCurrencyRepository {
    private final Map<Long, MainCurrencies> originalCurrency = new HashMap<>();
    private final Map<Long, MainCurrencies> targetCurrency = new HashMap<>();

    @Override
    public MainCurrencies getOriginalCurrency(long chatId) {
        return originalCurrency.getOrDefault(chatId, MainCurrencies.USD);
    }

    @Override
    public MainCurrencies getTargetCurrency(long chatId) {
        return targetCurrency.getOrDefault(chatId, MainCurrencies.USD);
    }

    @Override
    public void setOriginalCurrency(long chatId, MainCurrencies currency) {
        originalCurrency.put(chatId, currency);
    }

    @Override
    public void setTargetCurrency(long chatId, MainCurrencies currency) {
        targetCurrency.put(chatId, currency);
    }
}
