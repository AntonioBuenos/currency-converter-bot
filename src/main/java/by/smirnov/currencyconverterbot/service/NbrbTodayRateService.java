package by.smirnov.currencyconverterbot.service;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.TodayRateRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Service
@Slf4j
@RequiredArgsConstructor
public class NbrbTodayRateService implements TodayRateService {

    public static final String NBRB_TODAY_RATE_URL = "https://www.nbrb.by/api/exrates/rates?periodicity=0";
    public static final String NBRB_RATES_MESSAGE = "Официальные курсы валют, установленные Национальным банком РБ на %s г.:";
    public static final String DATE_PATTERN = "d MMMM yyyy";
    public static final String RATE_LINE_FORMAT = "%s (%d) = %.4f BYN";
    public static final String DELIMITER = "\n";

    private final TodayRateRepository repository;

    @Override
    public String getTodayRates() {

        List<Rate> rates = repository.findAllByDate(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)));
        if(rates.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            try {
                rates = mapper.readValue(new URL(NBRB_TODAY_RATE_URL), new TypeReference<>() {
                });
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            repository.saveAll(rates);
        }

        return getRatesInfo(rates);
    }

    private String getRatesInfo(List<Rate> rates) {
        Date date = rates.get(0).getDate();
        String header = String.format(NBRB_RATES_MESSAGE, formatDate(date));
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add(header);
        for (Rate rate : rates) {
            String rateInfo = String.format(RATE_LINE_FORMAT, rate.getName(), rate.getScale(), rate.getOfficialRate());
            joiner.add(rateInfo);
        }
        return joiner.toString();
    }

    private String formatDate(Date date){
        SimpleDateFormat formater = new SimpleDateFormat(DATE_PATTERN);
        return formater.format(date);
    }

}
