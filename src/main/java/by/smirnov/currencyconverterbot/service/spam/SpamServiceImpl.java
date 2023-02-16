package by.smirnov.currencyconverterbot.service.spam;

import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.entity.User;
import by.smirnov.currencyconverterbot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class SpamServiceImpl implements SpamService {

    private final BotExecutor executor;
    private final UserService userService;

    public void spam(String text) {
        userService.findAll()
                .forEach(user -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(user.getChatId());
                    message.setText(text);
                    message.enableHtml(true);
                    executor.executeMessage(message);
                });
    }
}
