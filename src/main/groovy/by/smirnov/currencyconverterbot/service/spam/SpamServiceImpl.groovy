package by.smirnov.currencyconverterbot.service.spam

import by.smirnov.currencyconverterbot.controller.BotExecutor
import by.smirnov.currencyconverterbot.service.user.UserService
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
@TupleConstructor(includes = ['executor', 'userService'], includeFields = true, includeProperties = false, force = true)
class SpamServiceImpl implements SpamService {

    @Autowired
    private final BotExecutor executor

    @Autowired
    private final UserService userService

    void spam(String text) {
        userService.findAll()
                .forEach {
                    SendMessage message = new SendMessage()
                    message.setChatId(it.chatId)
                    message.setText(text)
                    message.enableHtml(true)
                    executor.executeMessage(message)
                }
    }
}
