package by.smirnov.currencyconverterbot.service.conversion;

import by.smirnov.currencyconverterbot.entity.MainCurrencies;
import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.service.rate.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class NbrbCurrencyConversionServiceImpl implements CurrencyConversionService{

    private final RateService rateService;

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
        Rate todayRate = rateService.getTodayRate((long) currency.getId());
        double rate = todayRate.getOfficialRate();
        double scale = todayRate.getScale();

        return rate / scale;
    }
}
