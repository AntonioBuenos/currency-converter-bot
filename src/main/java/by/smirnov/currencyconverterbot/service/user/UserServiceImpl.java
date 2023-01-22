package by.smirnov.currencyconverterbot.service.user;

import by.smirnov.currencyconverterbot.entity.User;
import by.smirnov.currencyconverterbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;
import java.util.List;

import static by.smirnov.currencyconverterbot.constants.LogConstants.LOG_NEW_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Override
    public void registerUser(Message message) {
        if (repository.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            repository.save(user);
            log.info(LOG_NEW_USER, user);
        }
    }

    @Override
    public List<User> findAll() {
        return (List<User>) repository.findAll();
    }

    @Override
    public User findById(long id) {
        return repository.findById(id).orElse(null);
    }
}
