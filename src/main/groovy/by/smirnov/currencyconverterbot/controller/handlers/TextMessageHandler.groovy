package by.smirnov.currencyconverterbot.controller.handlers

import by.smirnov.currencyconverterbot.components.buttons.RateButtons
import by.smirnov.currencyconverterbot.components.commands.Commands
import by.smirnov.currencyconverterbot.controller.BotExecutor
import by.smirnov.currencyconverterbot.repository.ActualCommandRepository
import by.smirnov.currencyconverterbot.repository.MainCurrencyRepository
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService
import by.smirnov.currencyconverterbot.service.spam.SpamService
import by.smirnov.currencyconverterbot.util.DateParser
import by.smirnov.currencyconverterbot.util.DoubleParser
import groovy.transform.TupleConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Message

import static by.smirnov.currencyconverterbot.constants.CommonConstants.FORMAT_RATES_RESPONSE
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_STUB_REPLY

@Component
@TupleConstructor(
        includes = ['executor', 'mainCurrencyRepository', 'currencyConversionService', 'rateButtons', 'commandRepository', 'spamService'],
        includeFields = true, includeProperties = false, force = true
)
class TextMessageHandler {

    @Autowired
    private final BotExecutor executor

    @Autowired
    private final MainCurrencyRepository mainCurrencyRepository

    @Autowired
    private final CurrencyConversionService currencyConversionService

    @Autowired
    private final RateButtons rateButtons

    @Autowired
    private final ActualCommandRepository commandRepository

    @Autowired
    private final SpamService spamService

    void handleTextMessage(Message message) {
        long chatId = message.getChatId()

        def command = commandRepository.getActualCommand(chatId)
        def defautReply = String.format(MESSAGE_STUB_REPLY, message.getChat().getFirstName())
        if (!command) {
            executor.executeMessage(message, defautReply)
            return
        }

        switch (command) {
            case Commands.SET_CURRENCY -> handleConvertable(chatId, message)
            case Commands.SPAM -> spamService.spam(message.getText())
            case Commands.RATES_BY_DATE ->
                executor.executeMessage(rateButtons.getButtons(chatId, DateParser.parse(message.getText())))
            default -> executor.executeMessage(message, defautReply)
        }
    }

    private void handleConvertable(long chatId, Message message){
        def value = DoubleParser.parse(message.getText())
        if (value) {
            def original = mainCurrencyRepository.getOriginalCurrency(chatId)
            def target = mainCurrencyRepository.getTargetCurrency(chatId)
            def converted = currencyConversionService.convert(original, target, value)
            def rateMessage = String.format(FORMAT_RATES_RESPONSE, value, original, converted, target)
            executor.executeMessage(message, rateMessage)
        }
    }
}
