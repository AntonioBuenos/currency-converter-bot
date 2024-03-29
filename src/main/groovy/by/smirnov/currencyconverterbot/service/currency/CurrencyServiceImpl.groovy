package by.smirnov.currencyconverterbot.service.currency

import by.smirnov.currencyconverterbot.client.NbrbCurrencyClient
import by.smirnov.currencyconverterbot.entity.Currency
import by.smirnov.currencyconverterbot.repository.CurrenciesRepository
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
@TupleConstructor(includes = ['repository', 'client'], includeFields = true, includeProperties = false, force = true)
class CurrencyServiceImpl implements CurrencyService{

    @Autowired
    private final CurrenciesRepository repository

    @Autowired
    private final NbrbCurrencyClient client

    private static final String NOT_UPDATED = "Новых единиц валют не найдено"
    private static final String FAILED = "Полученный от НБРБ список отсутствует или не содержит записей"

    @Override
    String saveAll(){
        List<Currency> allCurrencies = client.getCurrencies()
        if(!allCurrencies) return FAILED
        int countNew = 0
        for (Currency currency : allCurrencies) {
            if(!repository.findById(currency.id)) {
                repository.save(currency)
                countNew++
            }
        }
        return countNew > 0 ? "Добавлено ${countNew} новых записей" : NOT_UPDATED
    }

    @Override
    Currency getActualCurrency(String abbreviation, LocalDate date){
        return repository
                .getActualCurrency(abbreviation, Timestamp.valueOf(LocalDateTime.of(date, LocalTime.MIN)))
                .orElse(null)
    }

}
