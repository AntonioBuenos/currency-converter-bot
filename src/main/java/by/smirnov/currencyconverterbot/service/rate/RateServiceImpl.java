package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.client.NbrbRateClient;
import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final RateRepository repository;
    private final NbrbRateClient nbrbRateClient;

    @Override
    public List<Rate> getDaylyRates(LocalDate date) {
        List<Rate> rates = repository.findAllByDate(date);
        if (rates.isEmpty()) rates = saveAndGetRates(date);
        return rates;
    }

    public Rate getRateByDate(String abbreviation, LocalDate date) {
        Optional<Rate> rate = repository.findRateByAbbreviationAndDate(abbreviation, date);
        if (rate.isPresent()) return rate.get();
        else checkAndSaveRates(date);
        return repository.findRateByAbbreviationAndDate(abbreviation, date).orElse(null);
    }

    public Rate getTodayRate(String abbreviation) {
        return getRateByDate(abbreviation, TODAY);
    }

    private void checkAndSaveRates(LocalDate date) {
        nbrbRateClient.getRates(date).stream()
                .filter(rate -> repository
                        .findRateByAbbreviationAndDate(rate.getAbbreviation(), rate.getDate())
                        .isEmpty())
                .forEach(repository::save);
    }

    private List<Rate> saveAndGetRates(LocalDate date) {
        List<Rate> rates = nbrbRateClient.getRates(date);
        return repository.saveAll(rates);
    }
}
