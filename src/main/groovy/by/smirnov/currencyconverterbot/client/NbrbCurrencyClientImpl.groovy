package by.smirnov.currencyconverterbot.client

import by.smirnov.currencyconverterbot.entity.Currency
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Component

import static by.smirnov.currencyconverterbot.constants.LogConstants.GET_CURRENCIES_ERROR

@Component
@Slf4j
class NbrbCurrencyClientImpl implements NbrbCurrencyClient{

    public static final String NBRB_ALL_CURRENCIES_URL = "https://www.nbrb.by/api/exrates/currencies"

    @Override
    List<Currency> getCurrencies() {
        def mapper = new ObjectMapper()
        List<Currency> allCurrencies = []
        try {
            allCurrencies = mapper.readValue(URI.create(NBRB_ALL_CURRENCIES_URL)::toURL(), new TypeReference<List<Currency>>() {})
        } catch (IOException e) {
            log.error(GET_CURRENCIES_ERROR, e.getMessage())
        }
        return allCurrencies;
    }
}