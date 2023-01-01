package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Currency;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NbrbTodayRateService implements TodayRateService{

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";

    @Override
    public Map<Currency, Double> getTodayRates(){
        Map<Currency, Double> todayRates = new HashMap<>();

        return todayRates;
    }
}
