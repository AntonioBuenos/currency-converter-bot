package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateRepository extends
        CrudRepository<Rate, Long>,
        JpaRepository<Rate, Long> {

    List<Rate> findAllByDate(LocalDate date);

    Optional<Rate> findRateByCurIdAndDate(Long curId, LocalDate date);

}
