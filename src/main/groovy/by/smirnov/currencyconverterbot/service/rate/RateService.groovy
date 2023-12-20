package by.smirnov.currencyconverterbot.service.rate

import by.smirnov.currencyconverterbot.entity.Rate

import java.time.LocalDate

interface RateService {

    List<Rate> getDaylyRates(LocalDate date)
    Rate getRateByDate(String abbreviation, LocalDate date)
    Rate getTodayRate(String abbreviation)
}