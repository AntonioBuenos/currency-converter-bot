package by.smirnov.currencyconverterbot.repository

import org.springframework.stereotype.Component

import java.time.LocalDate

@Component
class MapQueryDateRepository implements QueryDateRepository{

    private final HashMap<Long, LocalDate> dates = [:]

    @Override
    LocalDate getDate(Long chatId){
        dates.chatId
    }

    @Override
    void setDate(Long chatId, LocalDate date){
        dates.chatId = date
    }
}
