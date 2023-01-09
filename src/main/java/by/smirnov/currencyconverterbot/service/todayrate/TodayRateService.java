package by.smirnov.currencyconverterbot.service.todayrate;

import by.smirnov.currencyconverterbot.entity.Rate;

import java.time.LocalDate;
import java.util.List;

public interface TodayRateService {

    String getTodayRates();
    String getTodayMainRates();
    public Rate findTodayRate(Long curId, LocalDate date);
}
