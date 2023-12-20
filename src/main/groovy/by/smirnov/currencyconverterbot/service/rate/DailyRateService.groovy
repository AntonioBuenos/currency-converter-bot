package by.smirnov.currencyconverterbot.service.rate

import java.time.LocalDate

interface DailyRateService {

    String getRates(LocalDate date)

    String getRatesDynamic(LocalDate date)

    String getMainRates(LocalDate date)

    String getMainRatesDynamic(LocalDate date)
}