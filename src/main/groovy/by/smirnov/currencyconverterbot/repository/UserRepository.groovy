package by.smirnov.currencyconverterbot.repository

import by.smirnov.currencyconverterbot.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface UserRepository extends CrudRepository<User, Long>,
        JpaRepository<User, Long> {
}