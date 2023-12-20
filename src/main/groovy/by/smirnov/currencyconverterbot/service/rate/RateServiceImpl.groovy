package by.smirnov.currencyconverterbot.service.rate

import by.smirnov.currencyconverterbot.client.NbrbRateClient
import by.smirnov.currencyconverterbot.entity.Rate
import by.smirnov.currencyconverterbot.repository.RateRepository
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.time.LocalDate

import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY

@Service
@TupleConstructor(includes = ['repository', 'nbrbRateClient'], includeFields = true, includeProperties = false, force = true)
class RateServiceImpl implements RateService {

    @Autowired
    private final RateRepository repository

    @Autowired
    private final NbrbRateClient nbrbRateClient

    @Override
    List<Rate> getDaylyRates(LocalDate date) {
        repository.findAllByDate(date) ?: saveAndGetRates(date)
    }

    Rate getRateByDate(String abbreviation, LocalDate date) {
        def rate = repository.findRateByAbbreviationAndDate(abbreviation, date).orElse(null)
        return rate ?: saveAndGetOneRate(abbreviation, date)
    }

    Rate getTodayRate(String abbreviation) {
        getRateByDate(abbreviation, TODAY)
    }

    private Rate saveAndGetOneRate(String abbreviation, LocalDate date) {
        saveAndGetRates(date)
        repository.findRateByAbbreviationAndDate(abbreviation, date).orElse(null)
    }

    private List<Rate> saveAndGetRates(LocalDate date) {
        def rates = nbrbRateClient.getRates(date)
        repository.saveAll(rates) as List<Rate>
    }
}

