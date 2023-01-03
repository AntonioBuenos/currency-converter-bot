package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Currencies;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HashMapCurrencyRepository implements CurrencyRepository {
    private final Map<Long, Currencies> originalCurrency = new HashMap<>();
    private final Map<Long, Currencies> targetCurrency = new HashMap<>();

    public HashMapCurrencyRepository() {
        System.out.println("HASHMAP MODE is created");
    }

    @Override
    public Currencies getOriginalCurrency(long chatId) {
        return originalCurrency.getOrDefault(chatId, Currencies.USD);
    }

    @Override
    public Currencies getTargetCurrency(long chatId) {
        return targetCurrency.getOrDefault(chatId, Currencies.USD);
    }

    @Override
    public void setOriginalCurrency(long chatId, Currencies currency) {
        originalCurrency.put(chatId, currency);
    }

    @Override
    public void setTargetCurrency(long chatId, Currencies currency) {
        targetCurrency.put(chatId, currency);
    }
}
