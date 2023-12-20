package by.smirnov.currencyconverterbot.service.spam

import by.smirnov.currencyconverterbot.controller.BotExecutor
import by.smirnov.currencyconverterbot.service.user.UserService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
@RequiredArgsConstructor
class SpamServiceImpl implements SpamService {

    private final BotExecutor executor
    private final UserService userService

    void spam(String text) {
        userService.findAll()
                .forEach {
                    SendMessage message = new SendMessage()
                    message.setChatId(it.getChatId())
                    message.setText(text)
                    message.enableHtml(true)
                    executor.executeMessage(message)
                }
    }
}
