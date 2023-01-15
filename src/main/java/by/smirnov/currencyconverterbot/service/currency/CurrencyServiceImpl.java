package by.smirnov.currencyconverterbot.service.currency;

import by.smirnov.currencyconverterbot.client.NbrbCurrencyClient;
import by.smirnov.currencyconverterbot.entity.Currency;
import by.smirnov.currencyconverterbot.repository.CurrenciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{

    private final CurrenciesRepository repository;
    private final NbrbCurrencyClient client;

    @Override
    public void saveAll(){
        List<Currency> allCurrencies = client.getCurrencies();
        for (Currency currency : allCurrencies) {
            if(repository.findById(currency.getId()).isEmpty()) {
                repository.save(currency);
            }
        }
    }

}
