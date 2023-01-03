package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Currencies;

public interface CurrencyRepository {

    Currencies getOriginalCurrency(long chatId);

    Currencies getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currencies currency);

    void setTargetCurrency(long chatId, Currencies currency);
}
