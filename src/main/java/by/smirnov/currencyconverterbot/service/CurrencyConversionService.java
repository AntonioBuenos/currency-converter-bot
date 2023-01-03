package by.smirnov.currencyconverterbot.service;


import by.smirnov.currencyconverterbot.entity.Currencies;

public interface CurrencyConversionService {
        double getConversionRatio(Currencies original, Currencies target);
}
