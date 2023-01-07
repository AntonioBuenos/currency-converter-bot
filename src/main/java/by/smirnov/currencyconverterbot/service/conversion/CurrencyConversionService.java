package by.smirnov.currencyconverterbot.service.conversion;


import by.smirnov.currencyconverterbot.entity.MainCurrencies;

public interface CurrencyConversionService {
        Double convert(MainCurrencies original, MainCurrencies target, double value);
}
