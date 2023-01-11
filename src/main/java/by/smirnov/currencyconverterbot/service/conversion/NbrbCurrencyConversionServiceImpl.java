package by.smirnov.currencyconverterbot.service.conversion;

import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.service.rate.TodayRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Primary
public class NbrbCurrencyConversionServiceImpl implements CurrencyConversionService{

    private final TodayRateService todayRateService;

    @Override
    public Double convert(MainCurrencies original, MainCurrencies target, double value) {
        return value * getConversionRatio(original, target);
    }

    private double getConversionRatio(MainCurrencies original, MainCurrencies target) {
        double originalRate = getRate(original);
        double targetRate = getRate(target);
        return originalRate / targetRate;
    }

    private double getRate(MainCurrencies currency) {
        if (currency == MainCurrencies.BYN) {
            return 1;
        }
        Rate todayRate = todayRateService.findTodayRate((long) currency.getId(), LocalDate.now());
        double rate = todayRate.getOfficialRate();
        double scale = todayRate.getScale();

        return rate / scale;
    }
}
