package by.smirnov.currencyconverterbot.service.conversion;


import by.smirnov.currencyconverterbot.entity.Currencies;

public interface CurrencyConversionService {
        double getConversionRatio(Currencies original, Currencies target);
}
