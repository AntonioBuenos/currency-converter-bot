package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.util.DateFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class DailyRateServiceImpl implements DailyRateService {

    public static final String NBRB_RATES_MESSAGE =
            "<strong>Официальные курсы валют, установленные Национальным банком РБ на %s г.:</strong>";
    public static final String NO_RATES_MESSAGE = "<b><i>Информация о курсах на %s г. отсутствует</i></b>";
    public static final String RATE_NOT_FOUND = "курс не найден";
    public static final String RATE_LINE_FORMAT = "%s (%d) = %.4f BYN";
    public static final String DYNAMIC_RATE_LINE_FORMAT = "%s = %.4f BYN %s%s";
    public static final String DYNAMICS_FORMAT = " %s %.2f %%";
    public static final String SCALE_FORMAT = " (за %d %s)";
    public static final String EMPTY = "";
    public static final String RENMINBI = "Ұ";
    public static final String DOLLAR = "$";
    public static final String EURO = "€";
    public static final String RUS_RUBLE = "₽";
    public static final String UP = "↑";
    public static final String DOWN = "↓";
    public static final String USD = "USD";
    public static final String EUR = "EUR";
    public static final String RUB = "RUB";
    public static final String CNY = "CNY";
    public static final String DELIMITER = "\n";
    private static final String[] MAIN_CURRENCIES = {USD, EUR, RUB, CNY};
    private final RateService rateService;

    @Override
    public String getRates(LocalDate date) {
        List<Rate> rates = rateService.getDaylyRates(date);
        if (rates.isEmpty()) return String.format(NO_RATES_MESSAGE, DateFormatter.formatDate(date));
        return formatRatesInfo(rates, date);
    }

    @Override
    public String getMainRates(LocalDate date) {
        List<Rate> rates = new ArrayList<>();
        boolean isRateListEmpty = true;
        for (String abbreviation : MAIN_CURRENCIES) {
            Rate rate = rateService.getRateByDate(abbreviation, date);
            rates.add(rate);
            if(Objects.nonNull(rate)) isRateListEmpty = false;
        }
        if (isRateListEmpty) return String.format(NO_RATES_MESSAGE, DateFormatter.formatDate(date));
        return formatMainRatesDynamicInfo(rates, date);
    }

    private String formatRatesInfo(List<Rate> rates, LocalDate date) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        String header = String.format(NBRB_RATES_MESSAGE, DateFormatter.formatDate(date));
        joiner.add(header);
        for (Rate rate : rates) {
            String rateInfo;
            if (rate == null) rateInfo = RATE_NOT_FOUND;
            else rateInfo = String.format(RATE_LINE_FORMAT, rate.getName(), rate.getScale(), rate.getOfficialRate());
            joiner.add(rateInfo);
        }
        return joiner.toString();
    }

    private String formatMainRatesDynamicInfo(List<Rate> rates, LocalDate date) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        String header = String.format(NBRB_RATES_MESSAGE, DateFormatter.formatDate(date));
        joiner.add(header);
        for (Rate rate : rates) {
            String rateInfo;
            if (rate == null) rateInfo = RATE_NOT_FOUND;
            else {
                rateInfo = String.format(DYNAMIC_RATE_LINE_FORMAT,
                        getRateSymbol(rate),
                        rate.getOfficialRate(),
                        getDynamics(rate),
                        showScale(rate));
            }
            joiner.add(rateInfo);
        }
        return joiner.toString();
    }

    private String getRateSymbol(Rate rate) {
        String abbr = rate.getAbbreviation();
        String symbol;
        switch (abbr) {
            case USD -> symbol = DOLLAR;
            case EUR -> symbol = EURO;
            case RUB -> symbol = RUS_RUBLE;
            case CNY -> symbol = RENMINBI;
            default -> symbol = abbr;
        }
        return symbol;
    }

    private String showScale(Rate rate) {
        Long scale = rate.getScale();
        if (scale == 1) return EMPTY;
        return String.format(SCALE_FORMAT, scale, getRateSymbol(rate));
    }

    private String getDynamics(Rate rate) {
        double dayBeforeRate = rateService.getRateByDate(rate.getAbbreviation(), rate.getDate().minusDays(1)).getOfficialRate();
        double thisRate = rate.getOfficialRate();
        if (dayBeforeRate < thisRate) return String.format(DYNAMICS_FORMAT, UP, getPercentage(thisRate, dayBeforeRate));
        else if (dayBeforeRate > thisRate)
            return String.format(DYNAMICS_FORMAT, DOWN, getPercentage(thisRate, dayBeforeRate));
        else return EMPTY;
    }

    private double getPercentage(double main, double comparable) {
        return (comparable * 100 / main) - 100;
    }
}
