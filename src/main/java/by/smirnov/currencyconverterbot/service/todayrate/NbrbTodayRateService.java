package by.smirnov.currencyconverterbot.service.todayrate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.TodayRateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Service
@Slf4j
@RequiredArgsConstructor
public class NbrbTodayRateService implements TodayRateService {

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
    public static final String NBRB_RATES_MESSAGE = "Официальные курсы валют, установленные Национальным банком РБ на %s г.:";
    public static final String RATE_NOT_FOUND = "курс не найден";
    public static final String DATE_PATTERN = "d LLLL yyyy";
    public static final String RATE_LINE_FORMAT = "%s (%d) = %.4f BYN";
    public static final String DELIMITER = "\n";
    private static final Long[] MAIN_CUR_IDS = {431L, 451L, 456L};

    private final TodayRateRepository repository;

    @Override
    public String getTodayRates() {
        LocalDate date = LocalDate.now();
        List<Rate> rates = repository.findAllByDate(date);
        if (rates.isEmpty()) {
            rates = getAndSaveRates();
        }
        return formatRatesInfo(rates, date);
    }

    public String getTodayMainRates() {
        List<Rate> rates = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for (Long id : MAIN_CUR_IDS) {
            rates.add(findTodayRate(id, date));
        }
        return formatRatesInfo(rates, date);
    }

    private Rate findTodayRate(Long curId, LocalDate date) {
        Optional<Rate> rate = repository.findByCurIdAndDate(curId, date);
        if (rate.isPresent()) return rate.get();
        else getAndSaveRates();
        return repository.findByCurIdAndDate(curId, date).orElse(null);
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

    private List<Rate> getAndSaveRates() {
        List<Rate> rates = getRates();
        for (Rate rate : rates) {
            if (
                    repository.findByCurIdAndDate(rate.getCurId(), rate.getDate())
                            .isEmpty()
            ) repository.save(rate);
        }
        return rates;
    }

    private List<Rate> getRates(){
        List<Rate> rates = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        try {
            rates = mapper.readValue(new URL(NBRB_TODAY_RATE_URL), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return rates;
    }

}
