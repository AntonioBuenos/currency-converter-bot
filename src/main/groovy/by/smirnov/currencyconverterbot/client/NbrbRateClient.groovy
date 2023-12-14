package by.smirnov.currencyconverterbot.client

import by.smirnov.currencyconverterbot.entity.Rate

import java.time.LocalDate

interface NbrbRateClient {

    List<Rate> getRates(LocalDate date)
}