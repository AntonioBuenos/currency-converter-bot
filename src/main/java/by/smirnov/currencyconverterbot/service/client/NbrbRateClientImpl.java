package by.smirnov.currencyconverterbot.service.client;

import by.smirnov.currencyconverterbot.entity.Rate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NbrbRateClientImpl implements NbrbRateClient{
    public static final String NBRB_RATE_BY_DATE_URL = "https://www.nbrb.by/api/exrates/rates?ondate=%s&periodicity=0";
    public static final String DATE_PATTERN = "yyyy-M-D";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    @Override
    public List<Rate> getRates(LocalDate date){
        List<Rate> rates = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        try {
            URL url = new URL(String.format(NBRB_RATE_BY_DATE_URL, formatDate(date)));
            rates = mapper.readValue(url, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return rates;
    }

    private String formatDate(LocalDate date) {
        return date.format(formatter);
    }
}
