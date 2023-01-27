package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

public interface CurrenciesRepository extends
        CrudRepository<Currency, Long> {

    @Query("select c from Currency c where c.abbreviation = :abbreviation and c.dateStart < :date and c.dateEnd > :date")
    Optional<Currency> getActualCurrency(String abbreviation, Timestamp date);
}
