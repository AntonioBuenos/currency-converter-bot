package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Currency;

public interface CurrencyModeService {

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currency currency);

    void setTargetCurrency(long chatId, Currency currency);
}
