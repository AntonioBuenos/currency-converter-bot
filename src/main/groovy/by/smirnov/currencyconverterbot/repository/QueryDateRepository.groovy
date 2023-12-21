package by.smirnov.currencyconverterbot.repository

import java.time.LocalDate

interface QueryDateRepository {

    LocalDate getDate(Long chatId)
    void setDate(Long chatId, LocalDate date)
}