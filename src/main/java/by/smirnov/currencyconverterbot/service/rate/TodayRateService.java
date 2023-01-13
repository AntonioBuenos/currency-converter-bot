package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;

import java.time.LocalDate;

public interface TodayRateService {

    String getTodayRates();
    String getTodayMainRates();
}
