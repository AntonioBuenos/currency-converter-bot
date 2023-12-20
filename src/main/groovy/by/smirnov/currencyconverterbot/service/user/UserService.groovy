package by.smirnov.currencyconverterbot.service.user

import by.smirnov.currencyconverterbot.entity.User
import org.telegram.telegrambots.meta.api.objects.Message

interface UserService {

    void registerUser(Message message)

    List<User> findAll()

    User findById(long id)
}