package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class MapQueryDateRepository implements QueryDateRepository{

    private final Map<Long, LocalDate> dates = new HashMap<>();

    @Override
    public LocalDate getDate(Long chatId){
        return dates.get(chatId);
    }

    @Override
    public void setDate(Long chatId, LocalDate date){
        dates.put(chatId, date);
    }
}
