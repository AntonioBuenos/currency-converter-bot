package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Currency;

import java.util.Map;

public interface TodayRateService {

    Map<Currency, Double> getTodayRates();
}
