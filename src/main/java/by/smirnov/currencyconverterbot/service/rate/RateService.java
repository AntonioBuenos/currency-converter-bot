package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;

import java.time.LocalDate;
import java.util.List;

public interface RateService {
    List<Rate> getDaylyRates(LocalDate date);
    Rate getRateByDate(Long curId, LocalDate date);
    Rate getTodayRate(Long curId);
}
