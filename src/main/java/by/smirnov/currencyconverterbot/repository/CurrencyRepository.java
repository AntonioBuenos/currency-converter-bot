package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Currency;

public interface CurrencyRepository {

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currency currency);

    void setTargetCurrency(long chatId, Currency currency);
}
