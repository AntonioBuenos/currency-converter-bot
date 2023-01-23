package by.smirnov.currencyconverterbot.controller.handlers;

import by.smirnov.currencyconverterbot.components.buttons.DailyRateButtons;
import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons;
import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.service.currency.CurrencyService;
import by.smirnov.currencyconverterbot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import static by.smirnov.currencyconverterbot.components.commands.Commands.HELP;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_BY_DATE;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_TODAY;
import static by.smirnov.currencyconverterbot.components.commands.Commands.RATES_TOMORROW;
import static by.smirnov.currencyconverterbot.components.commands.Commands.SET_CURRENCY;
import static by.smirnov.currencyconverterbot.components.commands.Commands.SPAM;
import static by.smirnov.currencyconverterbot.components.commands.Commands.START;
import static by.smirnov.currencyconverterbot.components.commands.Commands.UPD_CURRENCIES;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TODAY;
import static by.smirnov.currencyconverterbot.constants.CommonConstants.TOMORROW;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_BAD_COMMAND;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_INPUT_DATE;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_SPAM;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_START;
import static by.smirnov.currencyconverterbot.constants.MessageConstants.MESSAGE_UNDER_CONSTRUCTION;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final BotExecutor executor;
    private final UserService userService;
    private final ExchangeButtons exchangeButtons;
    private final DailyRateButtons dailyRateButtons;
    private final CurrencyService currencyService;
    private final BotConfig botConfig;

    public void handleCommandMessage(Message message, MessageEntity commandEntity) {
        long chatId = message.getChatId();
        String command = message.getText().substring(commandEntity.getOffset(), commandEntity.getLength());

        if (START.equals(command)) {
            userService.registerUser(message);
            executor.executeMessage(message, MESSAGE_START);
        }
        else if (HELP.equals(command)) executor.executeMessage(message, MESSAGE_UNDER_CONSTRUCTION);
        else if (SET_CURRENCY.equals(command)) executor.executeMessage(exchangeButtons.getButtons(message));
        else if (RATES_BY_DATE.equals(command)) executor.executeMessage(message, MESSAGE_INPUT_DATE);
        else if (RATES_TODAY.equals(command)) executor.executeMessage(dailyRateButtons.getButtons(chatId, TODAY));
        else if (RATES_TOMORROW.equals(command)) executor.executeMessage(dailyRateButtons.getButtons(chatId, TOMORROW));
        else if (UPD_CURRENCIES.equals(command) && botConfig.getOwnerId() == chatId) {
            executor.executeMessage(message, currencyService.saveAll());
        } else if (SPAM.equals(command) && botConfig.getOwnerId() == chatId) {
            executor.executeMessage(message, MESSAGE_SPAM);
        } else executor.executeMessage(message, MESSAGE_BAD_COMMAND);
    }
}
