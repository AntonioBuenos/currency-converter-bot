package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CurrenciesRepository extends
        CrudRepository<Currency, Long>,
        JpaRepository<Currency, Long> {
}
