package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodayRateRepository extends
        CrudRepository<Rate, Long>,
        JpaRepository<Rate, Long> {

    List<Rate> findAllByDate(LocalDate date);

    @Query("SELECT r FROM Rate r WHERE r.curId = ?1 AND r.date = ?2")
    Optional<Rate> findByCurIdAndDate(Long curId, LocalDate date);
}
