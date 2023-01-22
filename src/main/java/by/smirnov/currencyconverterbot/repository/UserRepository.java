package by.smirnov.currencyconverterbot.repository;

import by.smirnov.currencyconverterbot.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
