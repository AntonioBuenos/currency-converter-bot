package by.smirnov.currencyconverterbot.client

import by.smirnov.currencyconverterbot.entity.Currency


interface NbrbCurrencyClient {

    List<Currency> getCurrencies()
}