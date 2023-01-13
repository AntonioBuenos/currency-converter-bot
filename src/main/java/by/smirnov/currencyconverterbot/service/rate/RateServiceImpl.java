package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.RateRepository;
import by.smirnov.currencyconverterbot.service.client.NbrbRateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateServiceImpl implements RateService{

    private final RateRepository repository;
    private final NbrbRateClient nbrbRateClient;

    @Override
    public List<Rate> getDaylyRates(LocalDate date) {
        return repository.findAllByDate(date);
    }

    public Rate getRateByDate(Long curId, LocalDate date) {
        Optional<Rate> rate = repository.findRateByCurIdAndDate(curId, date);
        if (rate.isPresent()) return rate.get();
        else getAndSaveRates();
        return repository.findByCurIdAndDate(curId, date).orElse(null);
    }

    public Rate getTodayRate(Long curId) {
        return getRateByDate(curId, LocalDate.now());
    }

    public Rate getTomorrowsRate(Long curId) {
        return getRateByDate(curId, LocalDate.now().plusDays(1));
    }

    private List<Rate> getAndSaveRates() {
        List<Rate> rates = nbrbRateClient.getRates();
        for (Rate rate : rates) {
            if (
                    repository.findByCurIdAndDate(rate.getCurId(), rate.getDate())
                            .isEmpty()
            ) repository.save(rate);
        }
        return rates;
    }
}
