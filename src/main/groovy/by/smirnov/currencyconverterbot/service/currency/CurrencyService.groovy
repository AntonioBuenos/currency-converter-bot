package by.smirnov.currencyconverterbot.service.currency

import by.smirnov.currencyconverterbot.entity.Currency

import java.time.LocalDate

interface CurrencyService {

    String saveAll()

    Currency getActualCurrency(String abbreviation, LocalDate date)
}