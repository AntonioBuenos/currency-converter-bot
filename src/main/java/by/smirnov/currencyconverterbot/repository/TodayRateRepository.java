package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TodayRateRepository extends
        CrudRepository<Rate, Long>,
        JpaRepository<Rate, Long> {

    List<Rate> findAllByDate(Timestamp date);
}
