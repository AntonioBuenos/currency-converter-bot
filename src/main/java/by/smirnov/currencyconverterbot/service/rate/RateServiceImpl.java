package by.smirnov.currencyconverterbot.service.rate;

import by.smirnov.currencyconverterbot.entity.Rate;
import by.smirnov.currencyconverterbot.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RateServiceImpl implements RateService{

    private final RateRepository repository;

    @Override
    public List<Rate> getDaylyRates(LocalDate date) {
        return repository.findAllByDate(date);
    }
}
