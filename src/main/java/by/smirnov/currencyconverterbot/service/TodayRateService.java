package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Rate;

import java.util.List;

public interface TodayRateService {

    String getTodayRates();
    String getTodayMainRates();
}
