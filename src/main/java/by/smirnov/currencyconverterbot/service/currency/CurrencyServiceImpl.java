package by.smirnov.currencyconverterbot.service.currency;

import by.smirnov.currencyconverterbot.client.NbrbCurrencyClient;
import by.smirnov.currencyconverterbot.entity.Currency;
import by.smirnov.currencyconverterbot.repository.CurrenciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{

    private final CurrenciesRepository repository;
    private final NbrbCurrencyClient client;
    private static final String NOT_UPDATED = "Новых единиц валют не найдено";
    private static final String UPDATED = "Добавлено %s новых записей";
    private static final String FAILED = "Полученный от НБРБ список отсутствует или не содержит записей";

    @Override
    public String saveAll(){
        List<Currency> allCurrencies = client.getCurrencies();
        if(allCurrencies == null || allCurrencies.isEmpty()) return FAILED;
        int countNew = 0;
        for (Currency currency : allCurrencies) {
            if(repository.findById(currency.getId()).isEmpty()) {
                repository.save(currency);
                countNew++;
            }
        }
        if(countNew > 0) return String.format(UPDATED, countNew);
        return NOT_UPDATED;
    }

    @Override
    public Currency getActualCurrency(String abbreviation, LocalDate date){
        return repository
                .getActualCurrency(abbreviation, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)))
                .orElse(null);
    }

}
