package by.smirnov.currencyconverterbot.service.rate;

import java.time.LocalDate;

public interface DailyRateService {

    String getRates(LocalDate date);
    String getMainRates(LocalDate date);
}
