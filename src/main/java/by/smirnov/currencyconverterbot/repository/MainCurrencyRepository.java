package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.MainCurrencies;

public interface MainCurrencyRepository {

    MainCurrencies getOriginalCurrency(long chatId);

    MainCurrencies getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, MainCurrencies currency);

    void setTargetCurrency(long chatId, MainCurrencies currency);
}
