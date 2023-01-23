package by.smirnov.currencyconverterbot.controller.handlers;

import by.smirnov.currencyconverterbot.components.buttons.DailyRateButtons;
import by.smirnov.currencyconverterbot.components.buttons.ExchangeButtons;
import by.smirnov.currencyconverterbot.components.commands.Commands;
import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.controller.BotExecutor;
import by.smirnov.currencyconverterbot.repository.ActualCommandRepository;
import by.smirnov.currencyconverterbot.service.currency.CurrencyService;
import by.smirnov.currencyconverterbot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

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
    private final ActualCommandRepository commandRepository;

    public void handleCommandMessage(Message message, MessageEntity commandEntity) {
        long chatId = message.getChatId();
        String textCommand = message.getText().substring(commandEntity.getOffset(), commandEntity.getLength());
        Commands command = Commands.findByCmd(textCommand);
        if (command == null) {
            executor.executeMessage(message, MESSAGE_BAD_COMMAND);
            return;
        } else commandRepository.setActualCommand(chatId, command);

        switch (command) {
            case START -> {
                userService.registerUser(message);
                executor.executeMessage(message, MESSAGE_START);
            }
            case HELP -> executor.executeMessage(message, MESSAGE_UNDER_CONSTRUCTION);
            case SET_CURRENCY -> executor.executeMessage(exchangeButtons.getButtons(message));
            case RATES_BY_DATE -> executor.executeMessage(message, MESSAGE_INPUT_DATE);
            case RATES_TODAY -> executor.executeMessage(dailyRateButtons.getButtons(chatId, TODAY));
            case RATES_TOMORROW -> executor.executeMessage(dailyRateButtons.getButtons(chatId, TOMORROW));
            case UPD_CURRENCIES -> {
                if (botConfig.getOwnerId() == chatId) executor.executeMessage(message, currencyService.saveAll());
            }
            case SPAM -> { if (botConfig.getOwnerId() == chatId) executor.executeMessage(message, MESSAGE_SPAM); }
        }
    }
}
