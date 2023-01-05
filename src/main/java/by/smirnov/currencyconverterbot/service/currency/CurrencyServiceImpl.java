package by.smirnov.currencyconverterbot.service.currency;

import by.smirnov.currencyconverterbot.entity.Currency;
import by.smirnov.currencyconverterbot.repository.CurrenciesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService{

    public static final String NBRB_ALL_CURRENCIES_URL = "https://www.nbrb.by/api/exrates/currencies";

    private final CurrenciesRepository repository;

    @Override
    public void saveAll(){
        ObjectMapper mapper = new ObjectMapper();
        List<Currency> allCurrencies = new ArrayList<>();
        try {
            allCurrencies = mapper.readValue(new URL(NBRB_ALL_CURRENCIES_URL), new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        for (Currency currency : allCurrencies) {
            if(repository.findById(currency.getId()).isEmpty()) {
                repository.save(currency);
            }
        }
    }

}
