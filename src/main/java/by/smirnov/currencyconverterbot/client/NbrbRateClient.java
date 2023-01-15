package by.smirnov.currencyconverterbot.client;

import by.smirnov.currencyconverterbot.entity.Rate;

import java.time.LocalDate;
import java.util.List;

public interface NbrbRateClient {

    List<Rate> getRates(LocalDate date);
}
