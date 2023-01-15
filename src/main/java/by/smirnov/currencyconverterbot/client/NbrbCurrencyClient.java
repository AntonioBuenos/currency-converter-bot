package by.smirnov.currencyconverterbot.client;

import by.smirnov.currencyconverterbot.entity.Currency;

import java.util.List;

public interface NbrbCurrencyClient {

    List<Currency> getCurrencies();
}
