package by.smirnov.currencyconverterbot.service.rate


import by.smirnov.currencyconverterbot.entity.Currency
import by.smirnov.currencyconverterbot.entity.MainCurrencies
import by.smirnov.currencyconverterbot.entity.Rate
import by.smirnov.currencyconverterbot.service.currency.CurrencyService
import by.smirnov.currencyconverterbot.util.DateFormatter
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDate

import static by.smirnov.currencyconverterbot.entity.MainCurrencies.BYN

@Service
@TupleConstructor(includes = ['rateService', 'currencyService'], includeFields = true, includeProperties = false, force = true)
class DailyRateServiceImpl implements DailyRateService {

    @Autowired
    private final RateService rateService

    @Autowired
    private final CurrencyService currencyService

    public static final String NBRB_RATES_MESSAGE = "<strong>Официальные курсы валют, установленные Национальным \
            банком РБ на %s г.:</strong>"
    public static final String NO_RATES_MESSAGE = "<b><i>Информация о курсах на %s г. отсутствует</i></b>"
    public static final String NO_CURRENCY_MESSAGE = "Информация о валюте отсутствует"
    public static final String RATE_NOT_FOUND = "курс не найден"
    public static final String RATE_LINE_FORMAT = "%s = %.4f BYN %s"
    public static final String DYNAMIC_RATE_LINE_FORMAT = "%s = %.4f BYN %s%s"
    public static final String DYNAMICS_FORMAT = " %s %.2f %%"
    public static final String SCALE_FORMAT = " (за %d %s)"
    public static final String EMPTY = ""
    public static final String NO_DYNAMIC = "без динамики"
    public static final String UP = "↑"
    public static final String DOWN = "↓"
    public static final String DELIMITER = "\n"

    @Override
    String getRates(LocalDate date){
        formatRatesInfo(getAllDailyRates(date), date)
    }

    @Override
    String getRatesDynamic(LocalDate date){
        formatRatesDynamicInfo(getAllDailyRates(date), date)
    }

    @Override
    String getMainRates(LocalDate date){
        formatRatesInfo(getMainDailyRates(date), date)
    }

    @Override
    String getMainRatesDynamic(LocalDate date){
        formatRatesDynamicInfo(getMainDailyRates(date), date)
    }

    private List<Rate> getAllDailyRates(LocalDate date) {
        rateService.getDaylyRates(date)
    }

    private List<Rate> getMainDailyRates(LocalDate date) {
        return Arrays.stream(MainCurrencies.values())
                .filter {it != BYN }
                .map {rateService.getRateByDate(String.valueOf(it), date) }
                .filter(Objects::nonNull)
                .toList()
    }

    private String formatRatesInfo(List<Rate> rates, LocalDate date) {
        if (!rates) return String.format(NO_RATES_MESSAGE, formatDate(date))
        def joiner = new StringJoiner(DELIMITER)
        def header = String.format(NBRB_RATES_MESSAGE, formatDate(date))
        joiner.add(header)
        rates.forEach {
            String rateInfo
            if (!it) rateInfo = RATE_NOT_FOUND
            else {
                Currency currency = currencyService.getActualCurrency(it.abbreviation, date)
                rateInfo = currency ?
                        String.format(RATE_LINE_FORMAT, currency.name, it.officialRate, showScale(it)) :
                        NO_CURRENCY_MESSAGE
            }
            joiner.add(rateInfo)
        }
        return joiner.toString()
    }

    private String formatRatesDynamicInfo(List<Rate> rates, LocalDate date) {
        if (!rates) return String.format(NO_RATES_MESSAGE, formatDate(date))
        def joiner = new StringJoiner(DELIMITER)
        def header = String.format(NBRB_RATES_MESSAGE, formatDate(date))
        joiner.add(header)
        rates.forEach {
            String rateInfo
            if (!it) rateInfo = RATE_NOT_FOUND
            else {
                rateInfo = String.format(DYNAMIC_RATE_LINE_FORMAT,
                        getRateSymbol(it),
                        it.officialRate,
                        getDynamics(it),
                        showScale(it))
            }
            joiner.add(rateInfo)
        }
        return joiner.toString()
    }

    private static String getRateSymbol(Rate rate) {
        String abbr = rate.abbreviation
        MainCurrencies currency
        try {
            currency = MainCurrencies.valueOf(abbr)
        }catch (IllegalArgumentException ignored){
            return abbr
        }
        return currency.symbol
    }

    private static String showScale(Rate rate) {
        Long scale = rate.scale
        return scale == 1 ? EMPTY : String.format(SCALE_FORMAT, scale, getRateSymbol(rate))
    }

    private String getDynamics(Rate rate) {
        def dayBeforeRate = rateService
                .getRateByDate(rate.abbreviation, rate.date.minusDays(1))
                .officialRate
        def thisRate = rate.officialRate
        if (dayBeforeRate < thisRate) return String.format(DYNAMICS_FORMAT, UP, getPercentage(thisRate, dayBeforeRate))
        else if (dayBeforeRate > thisRate)
            return String.format(DYNAMICS_FORMAT, DOWN, getPercentage(thisRate, dayBeforeRate)* (-1))
        else return NO_DYNAMIC
    }

    private static double getPercentage(double thisRate, double dayBeforeRate) {
        (thisRate * 100 / dayBeforeRate) - 100
    }

    private static String formatDate(LocalDate date) {
        DateFormatter.formatDate(date)
    }
}