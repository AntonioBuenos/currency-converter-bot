package by.smirnov.currencyconverterbot.client;

import by.smirnov.currencyconverterbot.entity.Currency;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.LogConstants.GET_CURRENCIES_ERROR;

@Component
@Slf4j
public class NbrbCurrencyClientImpl implements NbrbCurrencyClient{

    public static final String NBRB_ALL_CURRENCIES_URL = "https://www.nbrb.by/api/exrates/currencies";

    @Override
    public List<Currency> getCurrencies() {
        ObjectMapper mapper = new ObjectMapper();
        List<Currency> allCurrencies = new ArrayList<>();
        try {
            allCurrencies = mapper.readValue(new URL(NBRB_ALL_CURRENCIES_URL), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(GET_CURRENCIES_ERROR, e.getMessage());
        }
        return allCurrencies;
    }
}
