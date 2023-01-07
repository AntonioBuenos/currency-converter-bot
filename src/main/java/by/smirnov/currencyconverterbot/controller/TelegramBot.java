package by.smirnov.currencyconverterbot.controller;

import by.smirnov.currencyconverterbot.config.BotConfig;
import by.smirnov.currencyconverterbot.entity.Currencies;
import by.smirnov.currencyconverterbot.repository.CurrencyRepository;
import by.smirnov.currencyconverterbot.service.buttons.ExchangeButtonsService;
import by.smirnov.currencyconverterbot.service.commands.CommandListInit;
import by.smirnov.currencyconverterbot.service.conversion.CurrencyConversionService;
import by.smirnov.currencyconverterbot.service.message.MessageSender;
import by.smirnov.currencyconverterbot.service.buttons.TodayRateButtonsService;
import by.smirnov.currencyconverterbot.service.todayrate.TodayRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_LIST_INIT_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_SET_CURRENCY;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_START;
import static by.smirnov.currencyconverterbot.constants.Constants.COMMAND_TODAY_RATES;
import static by.smirnov.currencyconverterbot.constants.Constants.CONVERSION_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.DELIM;
import static by.smirnov.currencyconverterbot.constants.Constants.EDIT_MESSAGE_ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.ERROR;
import static by.smirnov.currencyconverterbot.constants.Constants.FORMAT_RATES_RESPONSE;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_BAD_COMMAND;
import static by.smirnov.currencyconverterbot.constants.Constants.MESSAGE_START;
import static by.smirnov.currencyconverterbot.constants.Constants.ORIGINAL;
import static by.smirnov.currencyconverterbot.constants.Constants.TARGET;
import static by.smirnov.currencyconverterbot.service.buttons.TodayRateButtonsServiceImpl.TODAY_ALL_CURRENCIES;
import static by.smirnov.currencyconverterbot.service.buttons.TodayRateButtonsServiceImpl.TODAY_MAIN_CURRENCIES;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final CurrencyRepository currencyRepository;
    private final CurrencyConversionService currencyConversionService;
    private final BotConfig botConfig;
    private final MessageSender messageSender;
    private final TodayRateService todayRateService;
    private final TodayRateButtonsService todayRateButtonsService;
    private final ExchangeButtonsService exchangeButtonsService;

    public TelegramBot(CurrencyRepository currencyRepository,
                       CurrencyConversionService currencyConversionService,
                       BotConfig botConfig,
                       MessageSender messageSender,
                       TodayRateService todayRateService,
                       TodayRateButtonsService todayRateButtonsService, ExchangeButtonsService exchangeButtonsService) {
        this.currencyRepository = currencyRepository;
        this.currencyConversionService = currencyConversionService;
        this.botConfig = botConfig;
        this.messageSender = messageSender;
        this.todayRateService = todayRateService;
        this.todayRateButtonsService = todayRateButtonsService;
        this.exchangeButtonsService = exchangeButtonsService;
        initCommandsList();
    }

    private void initCommandsList() {
        try {
            this.execute(
                    new SetMyCommands(CommandListInit.getCommands(), new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(COMMAND_LIST_INIT_ERROR, e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        long chatId = message.getChatId();
        int messageId = message.getMessageId();
        if (callbackData.equals(TODAY_MAIN_CURRENCIES)) {
            editMessage(todayRateService.getTodayMainRates(), chatId, messageId);
        } else if (callbackData.equals(TODAY_ALL_CURRENCIES)) {
            editMessage(todayRateService.getTodayRates(), chatId, messageId);
        } else processConversion(message, callbackData, chatId);
    }

    private void processConversion(Message message, String callbackData, long chatId) {
        String[] param = callbackData.split(DELIM);
        String action = param[0];
        Currencies newCurrency = Currencies.valueOf(param[1]);
        switch (action) {
            case ORIGINAL -> currencyRepository.setOriginalCurrency(chatId, newCurrency);
            case TARGET -> currencyRepository.setTargetCurrency(chatId, newCurrency);
        }
        executeMessage(exchangeButtonsService.getButtons(message, chatId));
    }

    private void editMessage(String text, long chatId, int messageId) {
        try {
            execute(EditMessageText.builder()
                    .chatId(chatId)
                    .text(text)
                    .messageId(messageId)
                    .build());
        } catch (TelegramApiException e) {
            log.error(EDIT_MESSAGE_ERROR, e.getMessage());
        }
    }

    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                handleCommandMessage(message, commandEntity.get());
                return;
            }
        }
        if (message.hasText()) {
            handleTextMessage(message);
        }
    }

    private void handleCommandMessage(Message message, MessageEntity commandEntity) {
        long chatId = message.getChatId();
        String command = this.getCommand(message, commandEntity);

        switch (command) {
            case COMMAND_START -> executeMessage(message, MESSAGE_START);
            case COMMAND_SET_CURRENCY -> executeMessage(exchangeButtonsService.getButtons(message));
            case COMMAND_TODAY_RATES -> executeMessage(todayRateButtonsService.getButtons(chatId));
            default -> executeMessage(message, MESSAGE_BAD_COMMAND);
        }
    }

    private void handleTextMessage(Message message) {
        Double value = parseDouble(message.getText());
        if (value!=null) {
            Currencies originalCurrency = currencyRepository.getOriginalCurrency(message.getChatId());
            Currencies targetCurrency = currencyRepository.getTargetCurrency(message.getChatId());
            double ratio = currencyConversionService.getConversionRatio(originalCurrency, targetCurrency);
            String rateMessage = String.format(FORMAT_RATES_RESPONSE,
                    value, originalCurrency, (value * ratio), targetCurrency);
            executeMessage(message, rateMessage);
        }
    }

    private Double parseDouble(String messageText) {
        try {
            return Double.parseDouble(messageText);
        } catch (NullPointerException | NumberFormatException e) {
            log.error(ERROR, e.getMessage());
            return null;
        }
    }

    private String getCommand(Message message, MessageEntity commandEntity) {
        return message
                .getText()
                .substring(commandEntity.getOffset(), commandEntity.getLength());
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR, e.getMessage());
        }
    }

    private void executeMessage(Message message, String messageText) {
        try {
            execute(messageSender.sendMessage(message, messageText));
        } catch (TelegramApiException e) {
            log.error(ERROR, e.getMessage());
        }
    }

    private void executeMessage(EditMessageReplyMarkup message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(CONVERSION_ERROR, e.getMessage());
        }
    }
}
