package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Currency;
import by.smirnov.currencyconverterbot.entity.Rate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NbrbTodayRateService implements TodayRateService{

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";

    @Override
    public Map<Currency, Double> getTodayRates(){
        Map<Currency, Double> todayRates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        List<Rate> rates = new ArrayList<>();
        try {
            rates = mapper.readValue(new URL(NBRB_TODAY_RATE_URL), new TypeReference<>(){});
        } catch (JsonProcessingException | MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Rate rate : rates) {
            System.out.println(rate);
        }
        return todayRates;
    }

    public static void main(String[] args) {
        new NbrbTodayRateService().getTodayRates();
    }
}
