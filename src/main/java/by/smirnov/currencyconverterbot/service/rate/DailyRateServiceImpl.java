package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
@RequiredArgsConstructor
public class DailyRateServiceImpl implements DailyRateService {

    public static final String NBRB_RATES_MESSAGE = "Официальные курсы валют, установленные Национальным банком РБ на %s г.:";
    public static final String RATE_NOT_FOUND = "курс не найден";
    public static final String DATE_PATTERN = "d LLLL yyyy";
    public static final String RATE_LINE_FORMAT = "%s (%d) = %.4f BYN";
    public static final String DELIMITER = "\n";
    private static final Long[] MAIN_CUR_IDS = {431L, 451L, 456L};

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
        for (Long id : MAIN_CUR_IDS) {
            rates.add(rateService.getTodayRate(id)); //to be changed
        }
        return formatRatesInfo(rates, date);
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

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return date.format(formatter);
    }
}
