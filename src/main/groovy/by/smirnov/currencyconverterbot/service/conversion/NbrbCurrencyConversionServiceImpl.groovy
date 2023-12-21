package by.smirnov.currencyconverterbot.service.conversion

import by.smirnov.currencyconverterbot.entity.MainCurrencies
import by.smirnov.currencyconverterbot.service.rate.RateService
import groovy.transform.TupleConstructor
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import static by.smirnov.currencyconverterbot.entity.MainCurrencies.BYN

@Service
@TupleConstructor(includes = ['rateService'], includeFields = true, includeProperties = false, force = true)
class NbrbCurrencyConversionServiceImpl implements CurrencyConversionService{

    @Autowired
    private final RateService rateService

    @Override
    Double convert(MainCurrencies original, MainCurrencies target, double value) {
        value * getConversionRatio(original, target)
    }

    private double getConversionRatio(MainCurrencies original, MainCurrencies target) {
        def originalRate = getRate(original)
        def targetRate = getRate(target)
        originalRate / targetRate
    }

    private double getRate(MainCurrencies currency) {
        currency == BYN ? 1 : countScale(currency)
    }

    private double countScale(MainCurrencies currency) {
        def todayRate = rateService.getTodayRate(String.valueOf(currency))
        def rate = todayRate.officialRate
        def scale = todayRate.scale

        rate / scale
    }
}
