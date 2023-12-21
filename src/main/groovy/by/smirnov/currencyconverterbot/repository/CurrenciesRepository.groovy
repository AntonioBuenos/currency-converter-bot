package by.smirnov.currencyconverterbot.repository

import by.smirnov.currencyconverterbot.entity.Currency
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

import java.sql.Timestamp

interface CurrenciesRepository extends
        CrudRepository<Currency, Long> {

    @Query("select c from Currency c where c.abbreviation = :abbreviation and c.dateStart < :date and c.dateEnd > :date")
    Optional<Currency> getActualCurrency(
            @Param("abbreviation") String abbreviation,
            @Param("date") Timestamp date
    )
}