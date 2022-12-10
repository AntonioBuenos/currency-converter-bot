package by.smirnov.currencyconverterbot.service;


import by.smirnov.currencyconverterbot.entity.Currency;

public interface CurrencyConversionService {
        double getConversionRatio(Currency original, Currency target);
}
