package by.smirnov.currencyconverterbot.client

import by.smirnov.currencyconverterbot.entity.Rate
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static by.smirnov.currencyconverterbot.constants.LogConstants.GET_RATES_ERROR

@Component
@Slf4j
class NbrbRateClientImpl implements NbrbRateClient{
    public static final String NBRB_RATE_BY_DATE_URL = "https://www.nbrb.by/api/exrates/rates?ondate=%s&periodicity=0"
    public static final String NBRB_QUERY_DATE_PATTERN = "yyyy-M-d"
    DateTimeFormatter queryDateFormatter = DateTimeFormatter.ofPattern(NBRB_QUERY_DATE_PATTERN)

    @Override
    List<Rate> getRates(LocalDate date){
        List<Rate> rates = []
        def mapper = new ObjectMapper().findAndRegisterModules()
        try {
            URL url = URI.create(String.format(NBRB_RATE_BY_DATE_URL, formatDate(date)))::toURL()
            rates = mapper.readValue(url, new TypeReference() {
            })
        } catch (IOException e) {
            log.error(GET_RATES_ERROR, e.getMessage())
        }
        return rates
    }

    private String formatDate(LocalDate date) {
        return date.format(queryDateFormatter)
    }
}