package by.smirnov.currencyconverterbot.controller.handlers

import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons
import by.smirnov.currencyconverterbot.components.buttons.RateButtons
import by.smirnov.currencyconverterbot.components.commands.Commands
import by.smirnov.currencyconverterbot.config.BotConfig
import by.smirnov.currencyconverterbot.controller.BotExecutor
import by.smirnov.currencyconverterbot.repository.ActualCommandRepository
import by.smirnov.currencyconverterbot.service.currency.CurrencyService
import by.smirnov.currencyconverterbot.service.user.UserService
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.MessageEntity

import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TOMORROW
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_BAD_COMMAND
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_INPUT_DATE
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_SPAM
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_START
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_UNDER_CONSTRUCTION

@Component
@TupleConstructor(
        includes = ['executor', 'userService', 'exchangeButtons', 'rateButtons', 'currencyService', 'botConfig', 'commandRepository'],
        includeFields = true, includeProperties = false, force = true
)
class CommandHandler {

    @Autowired
    private final BotExecutor executor

    @Autowired
    private final UserService userService

    @Autowired
    private final ExchangeButtons exchangeButtons

    @Autowired
    private final RateButtons rateButtons

    @Autowired
    private final CurrencyService currencyService

    @Autowired
    private final BotConfig botConfig

    @Autowired
    private final ActualCommandRepository commandRepository

    void handleCommandMessage(Message message, MessageEntity commandEntity) {
        long chatId = message.getChatId()
        def textCommand = message.getText().substring(commandEntity.getOffset(), commandEntity.getLength())
        def command = Commands.findByCmd(textCommand)
        if (!command) {
            executor.executeMessage(message, MESSAGE_BAD_COMMAND)
            return
        } else commandRepository.setActualCommand(chatId, command)

        switch (command) {
            case Commands.START -> {
                userService.registerUser(message)
                executor.executeMessage(message, MESSAGE_START)
            }
            case Commands.HELP -> executor.executeMessage(message, MESSAGE_UNDER_CONSTRUCTION)
            case Commands.SET_CURRENCY -> executor.executeMessage(exchangeButtons.getButtons(message))
            case Commands.RATES_BY_DATE -> executor.executeMessage(message, MESSAGE_INPUT_DATE)
            case Commands.RATES_TODAY -> executor.executeMessage(rateButtons.getButtons(chatId, TODAY))
            case Commands.RATES_TOMORROW -> executor.executeMessage(rateButtons.getButtons(chatId, TOMORROW))
            case Commands.UPD_CURRENCIES -> {
                if (botConfig.getOwnerId() == chatId) executor.executeMessage(message, currencyService.saveAll())
            }
            case Commands.SPAM -> { if (botConfig.getOwnerId() == chatId) executor.executeMessage(message, MESSAGE_SPAM) }
        }
    }
}
