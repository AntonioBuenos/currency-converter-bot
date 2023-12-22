package by.smirnov.currencyconverterbot.controller

import by.smirnov.currencyconverterbot.config.BotConfig
import by.smirnov.currencyconverterbot.controller.handlers.CallbackHandler
import by.smirnov.currencyconverterbot.controller.handlers.CommandHandler
import by.smirnov.currencyconverterbot.controller.handlers.TextMessageHandler
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.MessageEntity
import org.telegram.telegrambots.meta.api.objects.Update

import static by.smirnov.currencyconverterbot.constants.CommonConstants.COMMAND_KEY

@Component
@Slf4j
@TupleConstructor(includes = ['botConfig', 'callbackHandler', 'commandHandler', 'textMessageHandler'],
        includeFields = true, includeProperties = false, force = true)
class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private final BotConfig botConfig

    @Autowired
    private final CallbackHandler callbackHandler

    @Autowired
    private final CommandHandler commandHandler

    @Autowired
    private final TextMessageHandler textMessageHandler

    @Override
    String getBotUsername() {
        botConfig.getBotName()
    }

    @Override
    String getBotToken() {
        botConfig.getToken()
    }

    @Override
    void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            callbackHandler.handleCallback(update.getCallbackQuery())
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage())
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream()
                            .filter{COMMAND_KEY == it.getType() }
                            .findFirst()
            if (commandEntity) {
                commandHandler.handleCommandMessage(message, commandEntity.get())
                return
            }
        }
        if (message.hasText()) textMessageHandler.handleTextMessage(message)
    }

}
