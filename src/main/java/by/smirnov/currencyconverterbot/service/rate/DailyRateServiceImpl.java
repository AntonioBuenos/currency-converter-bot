package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
public class DailyRateServiceImpl implements DailyRateService {

    public static final String NBRB_RATES_MESSAGE =
            "<strong>Официальные курсы валют, установленные Национальным банком РБ на %s г.:</strong>";
    public static final String RATE_NOT_FOUND = "курс не найден";
    public static final String DATE_PATTERN = "d MMMM yyyy";
    public static final String RATE_LINE_FORMAT = "%s (%d) = %.4f BYN";
    public static final String DYNAMIC_RATE_LINE_FORMAT = "%s = %.4f BYN %s%s";
    public static final String DYNAMICS_FORMAT = " %s %.2f %%";
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

    private final RateRepository repository;
    private final RateService rateService;

    @Override
    public String getRates(LocalDate date) {
        List<Rate> rates = repository.findAllByDate(date);
        if (rates.isEmpty()) {
            rates = rateService.getDaylyRates(date);
        }
        return formatRatesInfo(rates, date);
    }

    @Override
    public String getMainRates(LocalDate date) {
        List<Rate> rates = new ArrayList<>();
        for (String abbreviation : MAIN_CURRENCIES) {
            rates.add(rateService.getRateByDate(abbreviation, date));
        }
        return formatMainRatesDynamicInfo(rates, date);//вернуть formatRatesInfo и сделать отдельный метод
    }

    private String formatRatesInfo(List<Rate> rates, LocalDate date) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        String header = String.format(NBRB_RATES_MESSAGE, formatDate(date));
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
        String header = String.format(NBRB_RATES_MESSAGE, formatDate(date));
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
        String symbol = null;
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
        if (scale == 1) return "";
        return String.format(" (за %d %s)", scale, getRateSymbol(rate));
    }

    private String getDynamics(Rate rate) {
        double dayBeforeRate = rateService.getRateByDate(rate.getAbbreviation(), rate.getDate().minusDays(1)).getOfficialRate();
        double thisRate = rate.getOfficialRate();
        if (dayBeforeRate < thisRate) return String.format(DYNAMICS_FORMAT, UP, getPercentage(thisRate, dayBeforeRate));
        else if (dayBeforeRate > thisRate)
            return String.format(DYNAMICS_FORMAT, DOWN, getPercentage(thisRate, dayBeforeRate));
        else return "";
    }

    private double getPercentage(double main, double comparable) {
        return (comparable * 100 / main) - 100;
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return date.format(formatter);
    }
}
