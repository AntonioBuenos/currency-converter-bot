package by.smirnov.currencyconverterbot.service.user

import by.smirnov.currencyconverterbot.entity.User
import by.smirnov.currencyconverterbot.repository.UserRepository
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

import java.sql.Timestamp

import static by.smirnov.currencyconverterbot.constants.LogConstants.LOG_NEW_USER

@Service
@TupleConstructor(includes = ['repository'], includeFields = true, includeProperties = false, force = true)
@Slf4j
class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository

    @Override
    void registerUser(Message message) {
        def chat = message.getChat()
        def chatId = message.getChatId()
        if (!repository.findById(chatId)) {
            User user = User.builder()
                    .chatId(chatId)
                    .firstName(chat.getFirstName())
                    .lastName(chat.getLastName())
                    .userName(chat.getUserName())
                    .registeredAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            repository.save(user)
            log.info(LOG_NEW_USER, user)
        }
    }

    @Override
    List<User> findAll() {
        repository.findAll() as List<User>
    }

    @Override
    User findById(long id) {
        repository.findById(id).orElse(null)
    }
}
