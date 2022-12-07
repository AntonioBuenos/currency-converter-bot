package by.smirnov.currencyconverterbot.service;


import by.smirnov.currencyconverterbot.entity.Currency;

public interface CurrencyConversionService {

        static CurrencyConversionService getInstance() {
            return new NbrbCurrencyConversionService();
        }

        double getConversionRatio(Currency original, Currency target);
}
