package by.smirnov.currencyconverterbot.config

import by.smirnov.currencyconverterbot.controller.TelegramBot
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Component
@Slf4j
@TupleConstructor(includes = ['bot'], includeFields = true, includeProperties = false, force = true)
class BotInitializer {

    @Autowired
    private final TelegramBot bot

    @EventListener( [ContextRefreshedEvent.class] )
    void init() throws TelegramApiException {
        def telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class)
        try {
            telegramBotsApi.registerBot(bot)
        } catch (TelegramApiException e) {
            log.error("Error occured: ${e.getMessage()}")
        }
    }
}
